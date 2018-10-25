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
    private String[] _categories;
    private ArrayList<HashMap<String, Integer>> _counters;

    /** Get the input files, run the reader, write to output.
     * Takes arguments of the parameters we want to list top ten lists.*/
	public static void main(String[] args) {
		initilize(args);
		ArrayList<Scanner> inputs = read_Input();
		for (int i = 0; i < inputs.size(); i++) {
            process(inputs.get(i));
		}
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
        HashMap<String, Integer> categories = new HashMap<>();

        if (s.hasNextLine()) {
            Scanner temp = new Scanner(s.nextLine());
            temp.useDelimiter(";");
            int i = 0;
            while (temp.hasNext()) {
				String cat_i = temp.next();
				if (categories.containsKey(cat_i)) {

				}
                i++;
            }
            while (s.hasNextLine()) {
            	temp = new Scanner(s.nextLine());
			}
        }
    }
}