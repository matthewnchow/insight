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
            _counters[i - 1] =  new ECHashMap();
            _categories[i - 1] = args[i];
        }
		FileReader[] inputs = read_Input();
		for (int i = 0; i < inputs.length; i++) {process(inputs[i]);}
		String[][] top10s = new String[_counters.length][10];
		for (int i = 0; i < _counters.length; i++) {
            top10s[i] = top_ten(_counters[i]);
        }
		write_out(top10s);
//        System.out.println("Done running");
//        System.out.print("Certified lines: " );
//        System.out.println(_certs);
//        System.out.println("Please check input folder for results");
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

    /** Returns true if CONTAINER contains all tokens of keys.*/
    public static boolean contains_all(String keys, String container) {
        Scanner scan_key = new Scanner(keys);
        scan_key.useDelimiter("-");
        while (scan_key.hasNext()) {
            if (!container.contains(scan_key.next())) {return false;}
        }
        return true;
    }

	/** Process each scanner. Count the states and jobs.
     *  Reads the first line, gets indices of important columns.
     *  Then scans through each line, adding to the states and jobs HashMaps
     *  for certified applications.*/
	private static void process(Reader r) {
	    int[] indices = new int[_categories.length];
	    int[] starts = new int[_categories.length];
	    int[] ends = new int[_categories.length];
	    boolean startend = false;
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
                            indices[j] = i;
                            break;
                        }
                    }
                }
                while ((line = R.readLine()) != null) {
                    if (line.contains(_certKEY)) {
                        _certs++;
                        int numquotes = 0;
                        int numsemicolons = 0;
                        startend = false;
                        for (int j = 0; j < line.length(); j++) {
                            if (line.charAt(j) == ';' || j == line.length() - 1) {
                                if (numquotes % 2 == 0) {
                                    if (startend) {
                                        ends[myUts.indexof(indices, numsemicolons - 1)] = j;
                                        startend = false;
                                    } else if (myUts.indexof(indices, numsemicolons) != -1) {
                                        starts[myUts.indexof(indices, numsemicolons)] = j + 1;
                                        startend = true;
                                    }
                                    numsemicolons += 1;
                                }
                            } else if (line.charAt(j) == '"') {
                                numquotes++;
                            }
                        }
                        for (int k = 0; k < indices.length; k++) {
                            _counters[k].put(line.substring(starts[k], ends[k]));
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

    /** Returns the keys with the 10 highest values in a HashMap.*/
    private static String[] top_ten(ECHashMap h) {
        String[] to_return = new String[10];
        int[] return_vals = new int[10];
        for (String s: h.keys()) {
            if (h.get(s) > myUts.min(return_vals)) {
                myUts.sorted_insert(h.get(s), s, return_vals, to_return);
            }
        }
        return to_return;
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
                    if (top10s[j][i] != null) {
                        w_top_ten.print(top10s[j][i] + ';'
                                + _counters[j].get(top10s[j][i]) + ';');
                        w_top_ten.print(String.format("%.1f", 100
                                * (float) _counters[j].get(top10s[j][i]) / _certs));
                        w_top_ten.print("%" + "\n");
                    }
                }
                w_top_ten.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
	    }
    }
}