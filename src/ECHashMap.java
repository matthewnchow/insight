/** A set of String values with associated count.
 * Structured as array of buckets, each containing a linked list.
 *  @author Matthew Chow
 */
class ECHashMap {

    /** Array of singly linked lists for storing hashmap.*/
    private Chain[] _buckets;
    /** Keeps track of the number of non-null linked list nodes.*/
    private int _size;
    /** Max tolerable loading factor. (Number of key-value pairs/number of buckets)*/
    private static final int MAXLOAD = 3;


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

    ECHashMap() {
        _size = 0;
        _buckets = new Chain[1];
        resize();
    }

    private int absHash(String s) {
        return (s.hashCode() & 0x7fffffff) % _buckets.length;
    }

    void put(String s) {
        if (s == null) {
            put("NULL");
            return;
        }
        if (!_buckets[absHash(s)].contains(s)) {_size += 1;}
        _buckets[absHash(s)].add(s);
        if (load() > MAXLOAD) {resize();}
    }

    /** Requires that head _str is unique.*/
    void put(Head h) {
        if (!_buckets[absHash(h.str())].contains(h.str())) {
            _size += 1;
            _buckets[absHash(h.str())].add(h);
        }
        if (load() > MAXLOAD) {resize();}
    }

    int get(String s) {
        return _buckets[absHash(s)].get(s);
    }

    boolean contains(String s) {
        return _buckets[absHash(s)].contains(s);
    }

    /** Returns the number of unique strings in hashmap.*/
    int size() {return _size;}

    /** Returns load factor: Number of items/number of buckets.*/
    float load() {
        return (float)_size/_buckets.length;
    }

    /** Increases number of buckets by factor of MAXLOAD. (Load = 1/MAXLOAD)*/
    private void resize() {
        Chain[] nb = new Chain[_buckets.length * MAXLOAD];
        Head[] temp = new Head[_size];
        for (int i = 0; i < _size;) {
            for (Chain bucket : _buckets) {
                while (bucket.size() > 0) {
                    temp[i] = bucket.headPop();
                    i++;
                }
            }
        }
        for (int i = 0; i < nb.length; i++) {
            nb[i] = new Chain();
        }
        _buckets = nb;
        _size = 0;
        for (int i = 0; i < temp.length; i++) {
            put(temp[i]);
        }
    }

    /** Returns array of all string keys in HashMap.*/
    String[] keys() {
        String[] result = new String[_size];
        for (int i = 0; i < _size;) {
            for (Chain bucket : _buckets) {
                Chain tempChain = bucket;
                while (tempChain.size() > 0) {
                    result[i] = tempChain.headStrPeek();
                    tempChain = tempChain._tail;
                    i++;
                }
            }
        }
        return result;
    }
}
