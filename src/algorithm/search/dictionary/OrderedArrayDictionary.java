package algorithm.search.dictionary;

import algorithm.search.RandomAssessable;
import com.sun.jdi.Value;

import java.security.KeyPair;
import java.util.*;
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
        if(size == 0) {
            return null;
        }
        int index = binarySearch(key, 0, size - 1);
        if(index < 0) {
            return null;
        }
        return remove(index);
    }

    @Override
    public Value put(Key key, Value value) {
        if(size == 0) {
            put(key, value, 0);
        }
        int index = binarySearch(key, 0, size - 1);
        if(index < 0) {
            put(key, value, - index -1);
            return null;
        } else {
            return set(value, index);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value compute(Key key, BiFunction<Key, Value, Value> remap) {
        if(size == 0) {
            Value value = remap.apply(key, null);
            if(value == null) {
                return null;
            }
            put(key, value, 0);
            return value;
        }

        int index = binarySearch(key, 0, size - 1);
        if(index < 0) {
            Value value = remap.apply(key, null);
            if(value == null) {
                return null;
            }
            put(key, value, - index - 1);
            return value;
        } else {
            Pair<Key, Value> pair = (Pair<Key, Value>) elements[index];
            Value value = remap.apply(key, pair.value);
            if(value == null) {
                remove(index);
            } else {
                set(value, index);
            }
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value get(Key key) {
        if(size == 0) {
            return null;
        }

        int index = binarySearch(key, 0, size - 1);
        if(index < 0) {
            return null;
        }
        return (Value) elements[index].value;
    }

    @Override
    public boolean contains(Key key) {
        if(size == 0) {
            return false;
        }

        return binarySearch(key, 0, size - 1) > 0;
    }

    @Override
    public Iterator<Key> keys() {
        return new KeyIterator();
    }

    @Override
    public Iterator<Pair<Key, Value>> entries() {
        return new PairIterator();
    }

    @Override
    public Iterator<Pair<Key, Value>> iterator() {
        return new PairIterator();
    }

    /* methods from random accessible */
    @SuppressWarnings("unchecked")
    @Override
    public Pair<Key, Value> elementAt(Integer integer) {
        if(integer < 0 || integer > size - 1) {
            throw new IndexOutOfBoundsException(integer);
        }
        return (Pair<Key, Value>) elements[integer];
    }

    @Override
    public Integer indexOf(Pair<Key, Value> keyValuePair, Integer from, Integer to) {
        if(size == 0) {
            return -1;
        }
        if(from > to) {
            throw new IllegalArgumentException("from must > to, from: " + from + ", to: " + to);
        }
        if(from < 0 || from > size - 1) {
            throw new IndexOutOfBoundsException("from index out of bounds: " + from);
        }
        if(to > size) {
            throw new IndexOutOfBoundsException("to index out bounds: " + to);
        }
        return binarySearch(keyValuePair.key, from, to - 1);
    }

    /* private methods */

    @SuppressWarnings("unchecked")
    private Value remove(int index) {
        Pair<?, ?> pair = elements[index];
        int moveSize = size - index - 1;
        System.arraycopy(elements,
                index + 1,
                elements,
                index,
                moveSize);
        // gc
        elements[size--] = null;
        return (Value) pair.value;
    }

    private void put(Key key, Value value, int index) {
        if(elements.length == size) {
            grow(size + 1);
        }

        int moveSize = size - index;
        System.arraycopy(elements, index, elements, index + 1, moveSize);
        elements[index] = new Pair<>(key, value);
        size++;
    }

    @SuppressWarnings("unchecked")
    private Value set(Value value, int index) {
        Pair<Key, Value> pair = (Pair<Key, Value>) elements[index];
        elements[index] = new Pair<>(pair.key, value);
        return pair.value;
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(Key key, int low, int high) {
        if(low > high) {
            throw new IllegalArgumentException("low index must > high index. low: " + low + ", high:" + high);
        }

        while (low <= high) {
            int middle = (low + high) / 2;
            Pair<?, ?> keyValuePair = elements[middle];
            int compareResult = key.compareTo((Key) keyValuePair.key);
            if(compareResult > 0) {
                // search key > middle key
                low = middle + 1;
            } else if(compareResult < 0) {
                high = middle - 1;
            } else {
                return middle;
            }
        }
        return -1 - low;
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


    private class KeyIterator implements Iterator<Key> {
        int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Key next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            return (Key)elements[index++].key;
        }

        @Override
        public void remove() {
            if(index < 1) {
                throw new NoSuchElementException();
            }

            OrderedArrayDictionary.this.remove(--index);
        }
    }

    private class PairIterator implements Iterator<Pair<Key, Value>> {
        int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Pair<Key, Value> next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            return (Pair<Key, Value>) elements[index++];
        }

        @Override
        public void remove() {
            if(index < 1) {
                throw new NoSuchElementException();
            }

            OrderedArrayDictionary.this.remove(--index);
        }
    }

    private static class Test {

        public static void main(String[] args) {
            testFrequencyCounter(1000);
            testRemove(1000);
            testClear(1000);
            testContain(1000);
        }

        static void testFrequencyCounter(int iteration) {
            System.out.println("Frequency Counter Test Case...");
            Random random = new Random(0);
            int[] frequencies = new int[20];
            Dictionary<Integer, Integer> dictionary = new OrderedArrayDictionary<>();
            int count = 0;
            while (count++ < iteration) {
                int key = random.nextInt(20);
                frequencies[key] += 1;
                dictionary.compute(key, (k, v) -> {
                    if(v == null) {
                        v = 1;
                    } else {
                        v += 1;
                    }
                    return v;
                });
            }
            System.out.println("Dictionary Frequency: ");
            dictionary.forEach(System.out::println);
            System.out.println("Correct Frequency: ");
            boolean result = true;
            for (int i = 0; i < frequencies.length; i++) {
                if(dictionary.get(i) != frequencies[i]) {
                    result = false;
                }
                System.out.println("<" + i + ", " + frequencies[i] + ">");
            }

            System.out.println("Frequency Counter Test Result: " + result);
            assert result : "Put Get Test Failed.";
        }

        static void testRemove(int iteration) {
            System.out.println("Remove Test Case...");
            Random random = new Random(0);
            int[] frequencies = new int[20];
            Dictionary<Integer, Integer> dictionary = new OrderedArrayDictionary<>();
            int count = 0;
            while (count++ < iteration) {
                int key = random.nextInt(20);
                frequencies[key] += 1;
                dictionary.compute(key, (k, v) -> {
                    if(v == null) {
                        v = 1;
                    } else {
                        v += 1;
                    }
                    return v;
                });
            }

            for(int i = 1; i < frequencies.length; i += 2) {
                dictionary.remove(i);
            }
            boolean result = true;
            System.out.println("Correct Frequency: ");
            for(int i = 0; i < frequencies.length; i += 2) {
                if(frequencies[i] != dictionary.get(i)) {
                    result = false;
                }
                System.out.println("<" + i + ", " + frequencies[i] + ">");
            }
            System.out.println("Dictionary Frequency: ");
            dictionary.forEach(System.out::println);

            result = result && dictionary.size() == Math.ceil(frequencies.length / 2d);
            System.out.println("Remove Test Result: " + result);
            assert result : "Remove Test Failed.";

        }

        static void testClear(int iteration) {
            System.out.println("Clear Test");
            Random random = new Random(0);
            Dictionary<Integer, Integer> dictionary = new OrderedArrayDictionary<>();
            int count = 0;
            while (count++ < iteration) {
                int key = random.nextInt(20);
                dictionary.compute(key, (k, v) -> {
                    if(v == null) {
                        v = 1;
                    } else {
                        v += 1;
                    }
                    return v;
                });
            }
            System.out.println("Dictionary Frequency: ");
            dictionary.forEach(System.out::println);
            dictionary.clear();
            System.out.println("Clear Result: " + dictionary.isEmpty());
            assert dictionary.isEmpty() : "Put Get Test Failed.";
        }

        static void testContain(int iteration) {
            System.out.println("Put Contain Test...");
            Random random = new Random(0);
            int[] frequencies = new int[20];
            Dictionary<Integer, Integer> dictionary = new ArrayDictionary<>();
            int count = 0;
            while (count++ < iteration) {
                int key = random.nextInt(20);
                frequencies[key] += 1;
                if(dictionary.contains(key)) {
                    dictionary.put(key, dictionary.get(key) + 1);
                } else {
                    dictionary.put(key, 1);
                }
            }

            System.out.println("Dictionary Frequency: ");
            dictionary.forEach(System.out::println);
            System.out.println("Correct Frequency: ");
            boolean result = true;
            for (int i = 0; i < frequencies.length; i++) {
                if(dictionary.get(i) != frequencies[i]) {
                    result = false;
                }
                System.out.println("<" + i + ", " + frequencies[i] + ">");
            }

            System.out.println("Put Contain Test Result: " + result);
            assert result : "Put Contain Test Failed.";
        }
    }

}
