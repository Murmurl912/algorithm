package algorithm.search.dictionary;

import java.util.*;

/**
 * dictionary is not a data structure
 * rather a abstract data type
 * dictionary map key to value
 * there are many implementations of dictionary
 * such as:
 *  unordered array
 *  ordered array
 *  ordered linked list, because normal linked cannot be indexed
 *      it takes no effect to sort list
 *      hence binary search cannot apply to sorted linked list
 *  ordered linked list
 *  binary search tree
 *  balanced binary search tree
 *  hash table based
 *
 * an important note is that while use dictionary
 * no matter what implementation the key in key and value pair
 * should be immutable or at effectively immutable
 * hence key should not be change once been put into dictionary
 *
 * in order to do that
 * an implementation can force key to be immutable
 * or clone the key before been stored
 *
 * @param <Key> key type
 * @param <Value> value type
 */
public interface Dictionary<Key, Value> {

    /**
     * size of dictionary
     * @return size of dictionary
     */
    public int size();

    /**
     * test if dictionary is empty
     * @return true if dictionary is empty
     */
    public boolean isEmpty();
    /**
     * capacity of dictionary
     * @return capacity of dictionary
     */
    public int capacity();

    /**
     * remove all key value pairs in dictionary
     */
    public void clear();

    /**
     * remove a key value pair
     * @param key key of key value pair to be removed
     * @return value of key value pair that been removed
     */
    public Value remove(Key key);

    /**
     * put a key value pair into dictionary
     * @param key key
     * @param value value
     * @return old value associated with key,
     *         if key did'nt occur in dictionary
     *         null will be returned
     */
    public Value put(Key key, Value value);

    /**
     * get value by associated key
     * @param key key of value that associate with
     * @return value of key if key find in dictionary,
     *         else null will be returned
     */
    public Value get(Key key);

    /**
     * test if dictionary contains given key
     * @param key existence of witch to be tested
     * @return true if dictionary contain given key
     */
    public boolean contains(Key key);

    /**
     * get an immutable collection of keys stored
     * in this dictionary
     * @return an immutable collection of keys in this dictionary
     */
    public Iterator<Key> keys();

    /**
     * get an immutable collection of
     * key value pair stored in this dictionary
     * @return an immutable collection of key value pair
     */
    public Iterator<Pair<Key, Value>> entries();

    /**
     * an immutable from outside package class
     * represent a key and value pair
     * @param <Key> type
     * @param <Value> value type
     */
    public static class Pair<Key, Value> {
        private Key key;
        private Value value;

        Pair(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        void set(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        void setKey(Key key) {
            this.key = key;
        }

        void setValue(Value value) {
            this.value = value;
        }

        public Key key() {
            return key;
        }

        public Value value() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return key.equals(pair.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

}
