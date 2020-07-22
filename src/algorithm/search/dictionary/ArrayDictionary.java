package algorithm.search.dictionary;

import java.util.Collection;
import java.util.Iterator;

/**
 * an unordered array implementation of dictionary
 *
 * @param <Key> type of key
 * @param <Value> type of value
 */
public class ArrayDictionary<Key, Value> implements Dictionary<Key, Value> {
    protected int size;
    protected Object[] elements;
    protected final int INITIAL_SIZE = 10;
    protected final static int MAXIMUM_SIZE = Integer.MAX_VALUE - 8;

    public ArrayDictionary() {
        elements = new Object[INITIAL_SIZE];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int capacity() {
        return elements.length;
    }

    @Override
    public void clear() {
        for(int i = size; i > 0; i--) {
            elements[i - 1] = null;
            size = i;
        }
    }

    @Override
    public Value remove(Key key) {
        return null;
    }

    @Override
    public Value put(Key key, Value value) {
        return null;
    }

    @Override
    public Value get(Key key) {
        return null;
    }

    @Override
    public boolean contains(Key key) {
        return false;
    }

    @Override
    public Iterator<Key> keys() {
        return null;
    }

    @Override
    public Iterator<Pair<Key, Value>> entries() {
        return null;
    }
}
