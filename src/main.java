import java.io.*;
import java.util.Scanner;

/** Program for taking a directory of input data and writing "top 10" files.
 *  Requires that input files have format:
 *      ;Col header1;Col header2;...Col headerN
 *      1;element (1,1);element (1,2);...element (1,N)
 *      .
 *      .
 *      .
 *      N;element (N,1);element (N,2);...element (N,N)
 *  Also requires consistent "Certification key"
 *      - only processes rows containing key (can be "" to process all)
 *  @author Matthew Chow  : matthewnchow@berkeley.edu*/
public class main {
    /** Certification key, can be null for automatic certification.*/
    private static String _certKEY;
    /** Count of certified entries.*/
    private static int _certs;
    /** HashMap (custom: String key, int value) containing the data we track.*/
    private static ECHashMap[] _counters;
    /** Array of keywords that specify which columns are relevant in each file.*/
    private static String[] _categories;

    /** Get the input files, run the reader, write to output.
     * Takes arguments of the parameters we want to list top ten lists.
	 * args[0] is required certification keyword (CERTIFIED for standard input).
	 * args[1]..args[n] are optional names of columns with categories to
	 * 		get top 10's for. For standard input:
     * 	    arg[1] = WORK-STATE
     * 	    arg[2] = JOB-TITLE */
	public static void main(String[] args) {
		_certKEY = args[0];
		_certs = 0;
        _counters = new ECHashMap[args.length - 1];
        _categories = new String[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            _counters[i] =  new ECHashMap();
            _categories[i] = args[i];
        }
		FileReader[] inputs = read_Input();
		for (int i = 0; i < inputs.length; i++) {process(inputs[i]);}
		String[][] top10s = new String[_counters.length][10];
		for (int i = 0; i < _counters.length; i++) {
            top10s[i] = top_ten(_counters[i]);
        }
		write_out(top10s);
	}

	/** Returns true if CONTAINER contains all tokens of keys.*/
    private static boolean contains_all(String keys, String container) {
	    Scanner scan_key = new Scanner(keys);
	    scan_key.useDelimiter("-");
	    while (scan_key.hasNext()) {
	        if (!container.contains(scan_key.next())) {return false;}
        }
        return true;
    }

    /** Returns the keys with the 10 highest values in a HashMap.*/
    private static String[] top_ten(ECHashMap h) {
	    String[] to_return = new String[10];
	    int[] return_vals = new int[10];
	    for (String s: h.keys()) {
	        if (h.get(s) > min(return_vals)) {
                sorted_insert(h.get(s), s, return_vals, to_return);
            }
        }
	    return to_return;
    }

    /** Inserts key and value into proper position in array,
     *  such that the array is ordered descending.
     *  Requires val > last element of val. */
    private static void sorted_insert(int val, String key, int[] vals, String[] keys) {
        for (int i = vals.length - 1; i >= 1; i--) {
            if (vals[i - 1] == 0 || val > vals[i - 1]
                    || (val == vals[i - 1] && key.compareTo(keys[i - 1]) < 0)) {
                vals[i] = vals[i-1];
                keys[i] = keys[i-1];
            } else {
                vals[i] = val;
                keys[i] = key;
                return;
            }
        }
        vals[0] = val;
        keys[0] = key;
    }

    /** Returns the minimum value of an array.*/
    private static int min(int[] a) {
        int min = 0;
	    for (int i = 0; i < a.length; i++) {
            if (a[i] < min) {min = a[i];}
        }
        return min;
    }

    /** Returns first index of value in array. -1 if does not contain. */
    private static int indexof(int[] a, int b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b) {return i;}
        }
        return -1;
    }

	/** Get all the names of the files in input,
	 * returns ArrayList of scanners (one for each file).*/
	private static FileReader[] read_Input() {
		File indir = new File("../input");
		File[] files = indir.listFiles();
        FileReader[] to_return = new FileReader[files.length];
        for (int i = 0; i < files.length; i++) {
		    try {
                to_return[i] = new FileReader(files[i]);
            } catch (FileNotFoundException e) {
		        e.printStackTrace();
            }
		}
		return to_return;
	}

	/** Process each scanner. Count the states and jobs.
     *  Reads the first line, gets indices of important columns.
     *  Then scans through each line, adding to the states and jobs HashMaps
     *  for certified applications.*/
	private static void process(Reader r) {
	    int[] indices = new int[_categories.length];
        BufferedReader R = new BufferedReader(r);
        Scanner temp;
        try {
            String line = R.readLine();
            if (line != null) {
                for (int j = 0; j < _categories.length; j++) { //Get col indices
                    temp = new Scanner(line).useDelimiter(";");
                    for (int i = 0; temp.hasNext(); i++) {
                        String cat_i = temp.next();
                        if (contains_all(_categories[j], cat_i)) {
                            indices[j] = i + 1;
                            break;
                        }
                    }
                }
                while ((line = R.readLine()) != null) {
                    if (line.contains(_certKEY)) {
                        _certs++;
                        temp = new Scanner(line).useDelimiter(";");
                        String data;
                        for (int i = 0; temp.hasNext(); i++) {
                            if (temp.hasNext("\".*?")) {
                                temp.useDelimiter("(\";|;\")");
                                data = temp.next();
                                temp.useDelimiter(";");
                                temp.next();
                            } else {data = temp.next();}
                            if (indexof(indices, i) != -1) {
                                _counters[indices[i]].put(data);
                            }
                        }
                    }
                }
            }
            R.close();
            r.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	/** Writes the output from the top ten keys in a HashMap to a text file.*/
    private static void write_out(String[][] top10s){
	    for (int j = 0; j < _categories.length; j++) {
	        String key = _categories[j];
            String name;
            if (key.contains("STATE")) {name = "states"; }
            else {name = "occupations";}
            File top_ten = new File("../output/top_10_" + name + ".txt");
	        try {
                PrintStream w_top_ten = new PrintStream(top_ten);
                w_top_ten.println("TOP_" + name.toUpperCase()
                        + ";NUMBER_CERTIFIED_APPLICATIONS;PERCENTAGE");
                for (int i = 0; i < top10s[j].length; i++) {
                    w_top_ten.print(top10s[j][i] + ';'
                        + _counters[i].get(top10s[j][i]) + ';');
                    w_top_ten.print(String.format("%.1f", 100
                        * (float)_counters[i].get(top10s[j][i])/_certs));
                    w_top_ten.println("%");
                }
                w_top_ten.print("\n");
                w_top_ten.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
	    }
    }
}