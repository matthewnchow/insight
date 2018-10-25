import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class main {
//    private HashMap<String, Integer> _states = new HashMap<>();
//    private HashMap<String, Integer> _jobs = new HashMap<>();
    private static ArrayList<String> _categories;
    private static HashMap<String, HashMap<String, Integer>> _counters;
    private static String _certKEY;
    private static int _certs;

    /** Get the input files, run the reader, write to output.
     * Takes arguments of the parameters we want to list top ten lists.
	 * args[0] is the name of the column with status.
	 * args[1]..args[n] are optional names of columns with categories to
	 * 		get top 10's for.*/
	public static void main(String[] args) {
//		if (args.length == 0) {
//			throw new Exception();
//		}
		_categories = new ArrayList<>();
		_counters = new HashMap<>();
		_certKEY = args[0];
		_certs = 0;
		for (int i = 1; i < args.length; i++) {
		    String str = args[i];
		    _categories.add(str);
		    _counters.put(str, new HashMap<>());
        }
		ArrayList<Scanner> inputs = read_Input();
		for (int i = 0; i < inputs.size(); i++) {
            process(inputs.get(i));
		}
		write_out();
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
     *  Reads the first line to get the title of each column.
     *  Then scans through each line, adding to the states and jobs HashMaps.*/
	private static void process(Scanner s) {
        HashMap<Integer, String> cat_idx = new HashMap<>();
        Scanner temp;
        String line;
        int catcount = 1;
        if (s.hasNextLine()) {
            temp = new Scanner(s.nextLine());
            temp.useDelimiter(";");
            for (int i = 0; temp.hasNext(); i++) {
				String cat_i = temp.next();
                System.out.print(cat_i + Integer.toString(catcount) + ";");
                catcount++;
				if (_categories.contains(cat_i)) {cat_idx.put(i+1, cat_i);}
            }
            System.out.println(cat_idx);
            int badlns = 0;
            while (s.hasNextLine()) {
                line = s.nextLine();
                if (line.contains(_certKEY)) {
                    _certs++;
                    temp = new Scanner(line);
                    temp.useDelimiter(";");
                    for (int i = 0; temp.hasNext(); i++) {
                        temp.useDelimiter(";");
                        if (temp.hasNext("(\").*")) {
                            temp.useDelimiter("(\".*;)");
                        }
                        String data = temp.next();
                        if (cat_idx.containsKey(i)) {
                            if (!data.matches("([A-Z][A-Z])")) {
                                badlns+=1;
                                System.out.println(line);
                            }

                            HashMap<String, Integer> temp_pointer =
                                    _counters.get(cat_idx.get(i));
                            if (temp_pointer.containsKey(data)) {
                                temp_pointer.replace(data, temp_pointer.get(data) + 1);
                            } else {temp_pointer.put(data, 1);}
                        }
                    }
                }
			}
            System.out.println(badlns);
        }
        System.out.println(_categories);
        System.out.println(_counters.get(_categories.get(0)));
//        System.out.println(_counters.get(_categories.get(1)));
        System.out.println(_certs);
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