package algorithm.search.dictionary;

import algorithm.search.RandomAssessable;
import com.sun.jdi.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BiFunction;

public class OrderedArrayDictionary<Key extends Comparable<Key>, Value>
        implements Dictionary<Key, Value>, RandomAssessable<Dictionary.Pair<Key, Value>, Integer> {

    /* protected fields */
    protected int size;
    protected Pair<?, ?>[] elements;
    protected static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE - 8;
    protected final int INITIAL_CAPACITY = 8;

    /* public constructors */
    public OrderedArrayDictionary() {
        elements = new Pair<?, ?>[INITIAL_CAPACITY];
    }

    /* public methods */
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
        for(int i = size - 1; i > - 1; i--) {
            elements[i] = null;
            size--;
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
    public Value compute(Key key, BiFunction<Key, Value, Value> remap) {
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

    @Override
    public Iterator<Pair<Key, Value>> iterator() {
        return null;
    }

    /* methods from random accessible */
    @Override
    public Pair<Key, Value> elementAt(Integer integer) {
        return null;
    }

    @Override
    public Integer indexOf(Pair<Key, Value> keyValuePair, Integer from, Integer to) {
        return null;
    }

    /* private methods */

    private Value remove(int index) {
        return null;
    }

    private int binarySearch(Key key, int low, int high) {
        return -1;
    }

    private Value put(Key key, Value value, int index) {
        return null;
    }

    private void grow(int miniCapacity) {

        if(miniCapacity < 0) {
            // int overflow
            throw new OutOfMemoryError();
        }

        // capacity grow proxy
        // grow 1.5 times of original size
        int desireCapacity = size + (size >> 1);

        int capacity = desireCapacity;
        // as mini capacity less or equal than Integer.MAX_VALUE
        // desire capacity less or equal than Integer.MAX_VALUE

        if(desireCapacity <= miniCapacity) {
            capacity = miniCapacity;
        } else {
            // desire capacity larger than maximum array size

            // this means if 1.5 * size > MAXIMUM_CAPACITY
            // capacity grow by mini capacity

            if(desireCapacity > MAXIMUM_CAPACITY) {
                // mini capacity larger than maximum array size
                // but mini capacity can never larger than Integer.MAX_VALUE

                if(miniCapacity > MAXIMUM_CAPACITY) {
                    // use Integer.MAX_VALUE as desire capacity
                    capacity = Integer.MAX_VALUE;
                } else {
                    capacity = miniCapacity;
                }
            }
        }

        // grow array
        Pair<?, ?>[] original = elements;
        elements = Arrays.copyOf(original, capacity);
    }



}
