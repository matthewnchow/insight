/** Class for unit testing of External Chaining HashMap class.
 *  Not compiled or used in run.sh*/
class ECHMTest {
    public static void main(String[] args) {
        System.out.print("Test PutGet passes:  ");
        System.out.println(testPutGet());
        System.out.print("Test Load passes:  ");
        System.out.println(testLoad());
        System.out.print("Test keys passes:  ");
        System.out.println(testKeys());
    }

    public static boolean testPutGet() {
        ECHashMap testMap = new ECHashMap();
        if (testMap.contains("A")) {return false;}
        testMap.put("A");
        if (!testMap.contains("A")) {return false;}
        testMap.put("A");
        testMap.put("B");
        if (testMap.get("A") != 2 && testMap.get("B") != 1) {return false;}
        String str1 = Character.toString('c');
        String str2 = "c";
        testMap.put(str1);
        testMap.put(str2);
        if (testMap.get(str1) != 2) {return false;}
        return true;
    }

    public static boolean testLoad() {
        ECHashMap testMap = new ECHashMap();
        for (int i = 0; i < 100000; i++) {
            testMap.put(Integer.toString(i));
            if (testMap.load() > 3 || testMap.load() < .3) {return false;}
        }
        testMap.put("0");
        testMap.put("0");
        if (testMap.size() != 100000) {return false;}
        return true;
    }

    public static boolean testKeys() {
        ECHashMap testMap = new ECHashMap();
        String[] keys = {"Apple", "Oranges", "Banana", "Pineapple", "84603"};
        for (String key : keys) {testMap.put(key);}
        System.out.println(toSTR(testMap.keys()));
        return true;
    }

    private static String toSTR(String[] a) {
        String result = "";
        for (int i = 0; i < a.length; i++) {
            result += a[i] + " ";
        }
        return result;
    }
}