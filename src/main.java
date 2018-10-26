import java.io.FileNotFoundException;
import java.util.Set;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
    /** Nested HashMap containing parameters we care about and their counts.*/
    private static HashMap<String, HashMap<String, Integer>> _counters;
    private static ArrayList<String> _categories;
    private static String _certKEY;
    private static int _certs;

    /** Get the input files, run the reader, write to output.
     * Takes arguments of the parameters we want to list top ten lists.
	 * args[0] is certification keyword (CERTIFIED for standard input).
	 * args[1]..args[n] are optional names of columns with categories to
	 * 		get top 10's for. For standard input:
     * 	    arg[1] = LCA_CASE_WORKLOC1_STATE
     * 	    arg[2] = */
	public static void main(String[] args) {
//		if (args.length == 0) {
//			throw new Exception();
//		}
		_certKEY = args[0];
		_certs = 0;
        _counters = new HashMap<>();
        _categories = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            _counters.put(args[i], new HashMap<>());
            _categories.add(args[i]);
        }
        System.out.println(_counters);
		ArrayList<Scanner> inputs = read_Input();
		for (int i = 0; i < inputs.size(); i++) {process(inputs.get(i));}
		write_out();
	}

    private static boolean contains_all(String keys, String container) {
	    Scanner scan_key = new Scanner(keys);
	    scan_key.useDelimiter("&");
	    while (scan_key.hasNext()) {
	        if (!container.contains(scan_key.next())) {return false;}
        }
        return true;
    }

	/** Get all the names of the files in input,
	 * returns ArrayList of scanners (one for each file).*/
	private static ArrayList<Scanner> read_Input() {
		ArrayList<Scanner> to_return = new ArrayList<>();
		File indir = new File("../input");
		File[] files = indir.listFiles();
		for (int i = 0; i < files.length; i++) {
		    try {
                to_return.add(new Scanner(files[i]));
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
	private static void process(Scanner s) {
        HashMap<Integer, String> cat_idx = new HashMap<>();
        Scanner temp;
        String line;
        if (s.hasNextLine()) {
            line = s.nextLine();
            for (String cat_keys : _categories)  {
                temp = new Scanner(line);
                temp.useDelimiter(";");
				for (int i = 0; temp.hasNext(); i++){
                    String cat_i = temp.next();
                    if (contains_all(cat_keys, cat_i)) {
                        cat_idx.put(i + 1, cat_keys);
                        break;
                    }
                }
            }
            int badlns = 0;
            int lines = 1;
            while (s.hasNextLine()) {
                lines++;
                line = s.nextLine();
                if (line.contains(_certKEY)) {
                    _certs++;
                    temp = new Scanner(line);
                    temp.useDelimiter(";");
                    String data;
                    int i = 0;
                    while (temp.hasNext()) {
                        if (temp.hasNext("\".*?")) {
                            temp.useDelimiter("(\";|;\")");
                            data = temp.next();
                            temp.useDelimiter(";");
                            temp.next();
                        } else {
                            data = temp.next();
                        }
                        if (cat_idx.containsKey(i)) {
                            if (!data.matches("\\s*?([A-Z][A-Z])\\s*?")) {
                                badlns+=1;
                            }
                            HashMap<String, Integer> temp_pointer =
                                    _counters.get(cat_idx.get(i));
                            if (temp_pointer.containsKey(data)) {
                                temp_pointer.replace(data, temp_pointer.get(data) + 1);
                            } else {temp_pointer.put(data, 1);}
                        }
                        i++;
                    }
                }
			}
            System.out.println("Lines "+ Integer.toString(lines));
            System.out.println("Bad Lines " + Integer.toString(badlns));
        }
        System.out.println(_counters);
        System.out.println("Certs "+ Integer.toString(_certs));
    }


    private static void write_out(){
	    for (String key : _counters.keySet()) {
	        File top_ten = new File("../output/top_10_" + key + ".txt");
//	        try {
//                FileWriter w_top_ten = new FileWriter(top_ten);
//            } catch (new Exception(FileNotFoundException e)) {
//
//            }
        }
    }
}