package insightSolution;

/** Linked List class to store buckets of data.
 * Always has last chain with head = null, tail = null)*/
class Chain {
    private Head _head;
    private Chain _tail;
    Chain() {init(null, null);}
    Chain(Head head, Chain tail) {init(head, tail);}
    void init(Head head, Chain tail) {
        _head = head;
        _tail = tail;
    }

    public Chain tail() {return _tail;}

    /** Add element to chain. */
    public void add(String s) {
        if (!contains(s)) {
            _tail = new Chain(_head, _tail);
            _head = new Head(s, 1);
        } else {
            headat(s).addone();
        }
    }

    /** Add element to chain. Requires that h is unique
     * (i.e. no two chains with same string key). */
    public void add(Head h) {
        _tail = new Chain(_head, _tail);
        _head = h;
    }

    /** Get length of chain. */
    public int size() {
        if (_head == null) {return 0;}
        else {return _tail.size() + 1;}
    }

    /** True iff chain contains string. */
    public boolean contains(String s) {
        if (_head ==  null) {return false;}
        else if (_head.equals(s)) {return true;}
        else {return _tail.contains(s);}
    }

    /** Returns the head containing string. */
    public Head headat(String s) {
        if (_head ==  null) {return null;}
        else if (_head.equals(s)) {return _head;}
        else {return _tail.headat(s);}
    }

    /** Returns count associated with key.*/
    public int get(String key) {return headat(key).count();}

    /** Removes head of chain if there is a head to remove.*/
    public Head headPop() {
        if (size() < 1) {return null;}
        Head temp = _head;
        _head = _tail._head;
        _tail = _tail._tail;
        return temp;
    }

    /** Returns string of head of chain without removing.*/
    public String headStrPeek() {
        if (size() < 1) {return null;}
        return _head.str();
    }
}