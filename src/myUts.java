/** Class containing various string and HashMap utilities.*/
public class myUts {

     /** ******************* Array Utilities ***************************/

    /** Returns the minimum value of an array.*/
    public static int min(int[] a) {
        int min = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] < min) {min = a[i];}
        }
        return min;
    }

    /** Returns first index of value in array. -1 if does not contain. */
    public static int indexof(int[] a, int b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b) {return i;}
        }
        return -1;
    }


    /** Inserts key and value into proper position in array,
     *  such that the array is ordered descending.
     *  Requires val > last element of val. */
    public static void sorted_insert(int val, String key, int[] vals, String[] keys) {
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
}