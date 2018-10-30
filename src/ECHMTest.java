/** Class for unit testing of External Chaining HashMap class.
 *  Not compiled or used in run.sh*/
class ECHMTest {
    public static void main(String[] args) {
        System.out.print("Test PutGet passes:  ");
        System.out.println(testPutGet());
        System.out.print("Test Load passes:  ");
        System.out.println(testLoad());

    }

    public static boolean testPutGet() {
        ECHashMap testMap = new ECHashMap();
        if (testMap.contains("A")) {return false;}
        testMap.put("A");
        if (!testMap.contains("A")) {return false;}
        testMap.put("A");
        testMap.put("B");
        if (testMap.get("A") != 2 && testMap.get("B") != 1) {return false;}
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
}