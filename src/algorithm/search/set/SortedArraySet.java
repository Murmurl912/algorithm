package algorithm.search.set;

import algorithm.search.RandomAssessable;

import java.util.*;

/**
 * todo: improve performance for union, complement and intersection operation between sorted set
 * an ordered array based set implementation
 * binary search is used
 */
public class SortedArraySet<E extends Comparable<E>> implements Set<E>, RandomAssessable<E, Integer> {
    protected int size;
    protected Comparable<?>[] elements;
    protected final int INITIAL_CAPACITY = 10;
    protected final int MAXIMUM_CAPACITY = Integer.MAX_VALUE - 8;

    public SortedArraySet() {
        elements = new Comparable<?>[INITIAL_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int capacity() {
        return elements.length;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = size; i > 0; i--) {
            elements[i - 1] = null;
            size--;
        }
    }

    @Override
    public boolean put(E element) {
        int index = 0;
        if(size > 0) { // set not empty search insert position
            index = binarySearch(element, 0, size - 1);
            if(index >= 0) { // not found
                return false;
            }
            index = - index - 1;
        }

        put(element, index);
        return true;
    }

    @Override
    public boolean contains(E element) {
        if(size == 0) {
            return false;
        }
        return binarySearch(element, 0, size - 1) >= 0;
    }

    @Override
    public boolean remove(E element) {
        if(size == 0) {
            return false;
        }
        int index = binarySearch(element, 0, size - 1);
        if(index < 0) {
            return false;
        }

        remove(index);
        return true;
    }

    @Override
    public Set<E> union(Set<E> that) {
        SortedArraySet<E> set = this.copy();
        for(E e : that) {
            set.put(e);
        }
        return set;
    }

    @Override
    public Set<E> intersect(Set<E> that) {
        SortedArraySet<E> set = new SortedArraySet<>();
        for(E e : this) {
            if(that.contains(e)) {
                // put last
                set.put(e, set.size);
            }
        }
        return set;
    }

    @Override
    public Set<E> complement(Set<E> that) {
        SortedArraySet<E> set = new SortedArraySet<>();
        for(E e : this) {
            if(!that.contains(e)) {
                // put last
                set.put(e, set.size);
            }
        }
        return set;
    }

    @Override
    public boolean isSubsetOf(Set<E> that) {
        if(this.size > that.size()) {
            return false;
        }
        for(E e : this) {
            if(!that.contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArraySetIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E elementAt(Integer integer) {
        if(integer < 0 || integer > size - 1) {
            throw new IndexOutOfBoundsException(integer);
        }

        return (E)elements[integer];
    }

    @Override
    public Integer indexOf(E e, Integer from, Integer to) {
        Objects.requireNonNull(e);
        if(size == 0) {
            return -1;
        }

        if(from < 0 || from > size - 1) {
            throw new IndexOutOfBoundsException(from);
        }

        if(to < 0 || to > size) {
            throw new IndexOutOfBoundsException(from);
        }

        if(from > to) {
            throw new IllegalArgumentException("from: " + from + " to: " + to);
        }

        return binarySearch(e, from, to - 1);
    }

    public void trim() {
        elements = Arrays.copyOf(elements, size);
    }

    private SortedArraySet<E> copy() {
        SortedArraySet<E> set = new SortedArraySet<>();
        set.elements = Arrays.copyOf(elements, size);
        set.size = size;
        return set;
    }

    private void put(E element, int index) {
        try {
            if(size == elements.length) {
                grow(size + 1);
            }

            int length = size - index;

            System.arraycopy(
                    elements,
                    index,
                    elements,
                    index + 1,
                    length);
            elements[index] = element;
            size++;
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        elements[size - 1] = null;
        size--;
    }

    @SuppressWarnings("unchecked")
    private int binarySearch(E element, int low, int high) {
        if(low > high) {
            throw new IllegalArgumentException("low: " + low + " high: " + high);
        }

        while (low <= high) {
            int middle = (low + high) / 2;
            E data = (E)elements[middle];
            int compareResult = element.compareTo(data);
            if(compareResult == 0) {
                // element = data
                return middle;
            } else if(compareResult > 0) {
                // element > data
                low = middle + 1;
            } else {
                // element < data
                high = middle - 1;
            }
        }
        return -(low + 1);
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
        Comparable<?>[] original = elements;
        elements = Arrays.copyOf(original, capacity);
    }

    private class ArraySetIterator implements  Iterator<E> {
        int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            return elementAt(index++);
        }

        @Override
        public void remove() {
            if(index < 1) {
                throw new NoSuchElementException();
            }
            SortedArraySet.this.remove(index);
            index--;
        }

    }

    @Override
    public String toString() {
        return "ArraySortedSet{" +
                "elements=" + Arrays.toString(elements) +
                '}';
    }

    private static class Test {

        public static void main(String[] args) {
            randomPutRemoveTest((int)(Math.abs(Math.random()) * 1000) + 1);
            randomContainTest((int)(Math.abs(Math.random()) * 1000) + 1);
            clearTest((int)(Math.abs(Math.random()) * 1000) + 1);
            unionTest((int)(Math.abs(Math.random()) * 1000) + 1, (int)(Math.abs(Math.random()) * 1000) + 1);
            intersectTest((int)(Math.abs(Math.random()) * 1000) + 1, (int)(Math.abs(Math.random()) * 1000) + 1);
            complementTest((int)(Math.abs(Math.random()) * 1000) + 1, (int)(Math.abs(Math.random()) * 1000) + 1);
            subsetTest((int)(Math.abs(Math.random()) * 1000) + 1, (int)(Math.abs(Math.random()) * 1000) + 1);
            ;
        }

        static void randomPutRemoveTest(int iteration) {
            System.out.println("Random Put Then Remove Test...");
            Set<Integer> set = new SortedArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            while (count++ < iteration) {
                int value = random.nextInt(100);
                set.put(value);
            }

            System.out.println("Set Before Remove: " + set);
            random.setSeed(0);
            while (count-- > 0) {
                int value = random.nextInt(100);
                set.remove(value);
            }
            System.out.println("Set After Remove: " + set);
            boolean result = set.isEmpty();
            System.out.println("Put Remove Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void randomContainTest(int iteration) {
            System.out.println("Random Contain Test...");
            Set<Integer> set = new SortedArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            while (count++ < iteration) {
                int value = random.nextInt(100);
                set.put(value);
            }

            boolean result = true;
            System.out.println("Set: " + set);
            random.setSeed(0);
            while (count-- > 0) {
                int value = random.nextInt(100);
                result = result && set.contains(value);
            }

            System.out.println("Contain Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void clearTest(int size) {
            System.out.println("Clear Set Test...");
            Set<Integer> set = new SortedArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            while (count++ < size) {
                int value = random.nextInt(100);
                set.put(value);
            }

            set.clear();
            boolean result = set.isEmpty();
            System.out.println("Clear Test Result: " + set.isEmpty());
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void unionTest(int sizeA, int sizeB) {
            System.out.println("Set Union Test...");
            Set<Integer> setA = new SortedArraySet<>();
            Set<Integer> setB = new SortedArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                setA.put(value);
            }
            count = 0;
            while (count++ < sizeB) {
                int value = random.nextInt(100);
                setB.put(value);
            }
            System.out.println("Set A: " + setB);
            System.out.println("Set B: " + setB);
            Set<Integer> setC = setA.union(setB);
            System.out.println("Union Result: " + setC);

            for(Integer integer : setA) {
                setC.remove(integer);
            }

            for(Integer integer : setB) {
                setC.remove(integer);
            }

            boolean result = setC.isEmpty();

            System.out.println("Union Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Union Test Failed");
            }
        }

        static void intersectTest(int sizeA, int sizeB) {
            System.out.println("Set Intersect Test...");
            Set<Integer> setA = new SortedArraySet<>();
            Set<Integer> setB = new SortedArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                setA.put(value);
            }
            System.out.println("Set A: " + setA);
            count = 0;
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                setB.put(value);
            }
            System.out.println("Set B: " + setB);

            Set<Integer> setC = setA.intersect(setB);
            boolean flag = true;
            for(Integer integer : setC) {

                if(!setA.contains(integer)
                        || !setB.contains(integer)) {
                    flag = false;

                    break;
                }

            }

            if(flag) {
                for(Integer integer : setA) {
                    if(!setC.contains(integer) && setB.contains(integer)) {
                        flag = false;
                        break;
                    }
                }
            }

            if(flag) {
                for(Integer integer : setB) {
                    if(!setC.contains(integer) && setA.contains(integer)) {
                        flag = false;
                        break;
                    }
                }
            }
            System.out.println("Intersect Result: " + setC);
            System.out.println("Intersect Test Result: " + flag);
            if(!flag) {
                throw new RuntimeException("Intersect Test Failed");
            }

        }

        static void complementTest(int sizeA, int sizeB) {
            System.out.println("Set Complement Test...");
            Set<Integer> setA = new SortedArraySet<>();
            Set<Integer> setB = new SortedArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                setA.put(value);
            }
            System.out.println("Set A: " + setA);
            count = 0;
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                setB.put(value);
            }
            System.out.println("Set B: " + setB);
            Set<Integer> setC = setA.complement(setB);

            boolean flag = true;
            for(Integer integer : setC) {

                if(setA.contains(integer)
                        && setB.contains(integer)) {
                    flag = false;

                    break;
                }

            }

            if(flag) {
                for(Integer integer : setA) {
                    // integer in a, but not in b and not c
                    if(!setB.contains(integer) && !setC.contains(integer)) {
                        flag = false;
                        break;
                    }
                }
            }

            System.out.println("Complement Result: " + setC);
            System.out.println("Complement Test Result: " + flag);
            if(!flag) {
                throw new RuntimeException("Complement Test Failed");
            }
        }

        static void subsetTest(int sizeA, int sizeB) {
            System.out.println("Is Subset Test...");

            Set<Integer> setA = new SortedArraySet<>();
            Set<Integer> setB = new SortedArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                setA.put(value);
            }
            System.out.println("Set A: " + setA);
            count = 0;
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                setB.put(value);
            }
            System.out.println("Set B: " + setB);
            boolean result =  !setA.isSubsetOf(setB);
            System.out.println("Negative Subset Test: " + result);


            setB.clear();
            result = result && setB.isSubsetOf(setA);
            System.out.println("Set B: " + setB);
            System.out.println("Empty Set Test: " + result);

            sizeB = (Math.abs((int)(Math.random() * sizeA) + 1) % (sizeA + 1));
            random.setSeed(0);
            count = 0;
            while (count++ < sizeB) {
                int value = random.nextInt(100);
                setB.put(value);
            }
            System.out.println("Set B: " + setB);
            result = result && setB.isSubsetOf(setA);
            System.out.println("Positive Subset Test: " + result);

            System.out.println("Is Subset Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Intersect Test Failed");
            }

        }
    }

}
