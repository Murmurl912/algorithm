package algorithm.search.dictionary;

import algorithm.search.RandomAssessable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * an unordered array implementation of dictionary
 *
 * @param <Key> type of key
 * @param <Value> type of value
 */
public class ArrayDictionary<Key, Value> implements Dictionary<Key, Value>, RandomAssessable<Dictionary.Pair<Key, Value>, Integer> {

    protected int size;
    protected Pair<?, ?>[] elements;
    protected final int INITIAL_CAPACITY = 10;
    protected final static int MAXIMUM_CAPACITY = Integer.MAX_VALUE - 8;

    public ArrayDictionary() {
        elements = new Pair<?, ?>[INITIAL_CAPACITY];
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
            size = i - 1;
            System.out.println(size);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value remove(Key key) {
        if(size == 0) {
            return null;
        }

        int index = indexOf(key);
        if(index < 0) {
            return null;
        }
        Pair<Key, Value> pair = (Pair<Key, Value>)elements[index];
        remove(index);
        return pair.value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value put(Key key, Value value) {
        int index = indexOf(key);
        if(index < 0) {
            put(new Pair<>(key, value), size);
            return null;
        }
        Pair<Key, Value> pair = (Pair<Key, Value>) elements[index];
        put(new Pair<>(key, value), index);
        return pair.value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value compute(Key key, BiFunction<Key, Value, Value> remapping) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(remapping);
        Value oldValue = null;
        int index = indexOf(key);
        if(index > -1) {
            oldValue = ((Pair<Key, Value>) elements[index]).value;
        } else {
            index = size;
        }
        Value value = remapping.apply(key, oldValue);
        if(value != null) {
            put(new Pair<>(key, value), index);
        } else { // value == null
            // delete key
            if(index < size) {
                remove(index);
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value get(Key key) {
        if(size == 0) {
            return null;
        }
        int index = indexOf(key);
        if(index < 0) {
            return null;
        }

        return ((Pair<Key, Value>)elements[index]).value;
    }

    @Override
    public boolean contains(Key key) {
        if(size == 0) {
            return false;
        }
        return indexOf(key) > -1;
    }

    @Override
    public Iterator<Key> keys() {
        return new KeyIterator();
    }

    @Override
    public Iterator<Pair<Key, Value>> entries() {
        return new PairIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<Key, Value> elementAt(Integer integer) {
        if(integer < 0 || integer > size - 1) {
            throw new IndexOutOfBoundsException(integer);
        }

        return (Pair<Key, Value>)elements[integer];
    }

    @Override
    public Integer indexOf(Pair<Key, Value> keyValueKeyPair,
                           Integer from, Integer to) {
        if(from < 0 || from > size -1) {
            throw new IndexOutOfBoundsException(from);
        }

        if(to < 0 || to > size) {
            throw new IndexOutOfBoundsException(to);
        }

        if(from > to) {
            throw new IllegalArgumentException("from must >= to, from: " + from + ", to: " + to);
        }

        for (int i = 0; i < size; i++) {
            if(Objects.equals(elements[i].key, keyValueKeyPair.key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<Pair<Key, Value>> iterator() {
        return new PairIterator();
    }

    @SuppressWarnings("unchecked")
    private int indexOf(Key key) {
        for (int i = 0; i < size; i++) {
            if(Objects.equals(((Pair<Key, Value>)elements[i]).key, key)) {
                return i;
            }
        }
        return -1;
    }

    private void remove(int index) {
        int length = size - index - 1;
        System.arraycopy(
                elements,
                index + 1,
                elements,
                index,
                length);
        // garbage collect
        elements[--size] = null;
    }

    private void put(Pair<Key, Value> keyValuePair, int index) {
        if(index == size) {
            grow(size + 1);
            size++;
        }
        elements[index] = keyValuePair;
    }

    private void grow(int miniCapacity) {
        if(miniCapacity < 0) {
            // int overflow
            throw new OutOfMemoryError();
        }

        // capacity grow proxy
        int desireCapacity = size + (size >> 1);
        int capacity = desireCapacity;
        // as mini capacity less or equal than Integer.MAX_VALUE
        // desire capacity less or equal than Integer.MAX_VALUE

        if(desireCapacity <= miniCapacity) {
            capacity = miniCapacity;
        } else {
            // desire capacity larger than maximum array size
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
            if(size < 1) {
                throw new NoSuchElementException();
            }

            ArrayDictionary.this.remove(index--);
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
            if(size < 1) {
                throw new NoSuchElementException();
            }

            ArrayDictionary.this.remove(index--);
        }
    }

    private static class Test {

        public static void main(String[] args) {
//            testFrequencyCounter(100);
//            testRemove(100);
//            testClear(100);
            testContain(100);
        }

        static void testFrequencyCounter(int iteration) {
            System.out.println("Frequency Counter Test Case...");
            Random random = new Random(0);
            int[] frequencies = new int[20];
            Dictionary<Integer, Integer> dictionary = new ArrayDictionary<>();
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
            Dictionary<Integer, Integer> dictionary = new ArrayDictionary<>();
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
            Dictionary<Integer, Integer> dictionary = new ArrayDictionary<>();
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
