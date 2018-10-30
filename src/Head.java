/** Class for putting in the head of Linked List objects.
 * Contains string (key) and int (value).
 * Can only increase value.*/
class Head {
    private String _str;
    private int _count;
    Head(String s, int c) {
        _str = s;
        _count = c;
    }
    public String str() {return _str;}

    public void addone() {_count += 1;}

    public int count() {return _count;}

    @Override
    public boolean equals(Object o) {
        return _str.compareTo((String) o) == 0;
    }
}
