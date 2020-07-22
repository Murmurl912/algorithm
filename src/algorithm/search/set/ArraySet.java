package algorithm.search.set;

import algorithm.search.RandomAssessable;

import java.util.*;

/**
 * an unordered implementation of set
 * element stored in set should be immutable
 * or effectively immutable
 * @param <E> type of element to store in this set
 */
public class ArraySet<E>
        implements Set<E>, RandomAssessable<E, Integer> {

    protected int size;
    protected Object[] elements;
    public static final int INITIAL_CAPACITY = 10;
    public static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE - 8;

    public ArraySet() {
        elements = new Object[INITIAL_CAPACITY];
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
        Objects.requireNonNull(element);
        if(indexOf(element, 0, size) != -1) {
            return false;
        }

        if(elements.length == size) {
            ensureCapacity(size + 1);
        }
        elements[size++] = element;
        return true;
    }

    @Override
    public boolean contains(E element) {
        if(size == 0) {
            return false;
        }
        return indexOf(element, 0, size) > -1;
    }

    @Override
    public boolean remove(E element) {
        if(size == 0) {
            return false;
        }
        int index = indexOf(element, 0, size);
        if(index == -1) {
            return false;
        }

        remove(index);
        return true;
    }


    @Override
    public Iterator<E> iterator() {
        return new ArraySetIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E elementAt(Integer integer) {
        ensureIndexInBounds(integer);
        return ((E)elements[integer]);
    }

    @Override
    public Integer indexOf(E e, Integer from, Integer to) {
        if(size == 0) {
            return -1;
        }
        ensureIndexInBounds(from);
        if(to < 0 || to > size) {
            throw new IndexOutOfBoundsException(to);
        }

        for(int i = from; i < to; i++) {
            if(Objects.equals(e, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Set<E> union(Set<E> that) {
        Set<E> set = this.copy();
        for(E e : that) {
            set.put(e);
        }
        return set;
    }

    @Override
    public Set<E> intersect(Set<E> that) {
        ArraySet<E> set = new ArraySet<>();
        for(E thatElement : that) {
           if(this.contains(thatElement)) {
               set.putLast(thatElement);
           }
        }
        return set;
    }

    @Override
    public Set<E> complement(Set<E> that) {
        ArraySet<E> set = new ArraySet<>();
        for(E thisElement : this) {
            if(that.contains(thisElement)) {
                continue;
            }
            set.putLast(thisElement);
        }
        return set;
    }

    @Override
    public boolean isSubsetOf(Set<E> that) {
        if(this.size() > that.size()) {
            return false;
        }

        for(E thisElement : this) {
            if(!that.contains(thisElement)) {
                return false;
            }
        }

        return true;
    }

    public void trim() {
        elements = Arrays.copyOf(elements, size);
    }

    protected ArraySet<E> copy() {
        ArraySet<E> set = new ArraySet<>();
        set.size = size;
        set.elements = Arrays.copyOf(elements, size);
        return set;
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

    private void putLast(E element) {
        if(size == elements.length) {
            ensureCapacity(size + 1);
        }
        elements[size++] = element;
    }

    private void ensureIndexInBounds(int index) {
        if(index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(index);
        }
    }

    private void ensureCapacity(int minCapacity) {
        grow(minCapacity);
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
        Object[] original = elements;
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
            if(index < 0) {
                throw new NoSuchElementException();
            }
            ArraySet.this.remove(index);
            index--;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if(!(o instanceof Set)) {
            return false;
        }

        Set<E> set = (Set<E>) o;
        if(set.size() != size) {
            return false;
        }

        return this.isSubsetOf(set);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        for(int i = 0; i < size; i++) {
            result += elements[i].hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        return "ArraySet{" +
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
            Set<Integer> set = new ArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put: ");
            while (count++ < iteration) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + set.put(value) + ", " + set);
            }
            System.out.println("Remove: ");
            random.setSeed(0);
            while (count-- > 0) {
                int value = random.nextInt(100);
                System.out.println("Remove: " + value + ", " + set.remove(value) + ", " + set);
            }
            boolean result = set.isEmpty();
            System.out.println("Put Remove Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void randomContainTest(int iteration) {
            Set<Integer> set = new ArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put: ");
            while (count++ < iteration) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + set.put(value) + ", " + set);
            }

            boolean result = true;
            System.out.println("Contain: ");
            random.setSeed(0);
            while (count-- > 0) {
                int value = random.nextInt(100);
                result = result && set.contains(value);
                System.out.println("Contain: " + value + ", " + set.contains(value) + ", " + set);
            }

            System.out.println("Contain Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void clearTest(int size) {
            Set<Integer> set = new ArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put: ");
            while (count++ < size) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + set.put(value) + ", " + set);
            }

            set.clear();
            boolean result = set.isEmpty();
            System.out.println("Clear Test Result: " + set.isEmpty());
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void unionTest(int sizeA, int sizeB) {
            Set<Integer> setA = new ArraySet<>();
            Set<Integer> setB = new ArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put Set A: ");
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setA.put(value) + ", " + setA);
            }
            count = 0;
            System.out.println("Put Set B: ");
            while (count++ < sizeB) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setB.put(value) + ", " + setB);
            }

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
            Set<Integer> setA = new ArraySet<>();
            Set<Integer> setB = new ArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put Set A: ");
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setA.put(value) + ", " + setA);
            }
            count = 0;
            System.out.println("Put Set B: ");
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                System.out.println("Add: " + value + ", " + setB.put(value) + ", " + setB);
            }

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
            Set<Integer> setA = new ArraySet<>();
            Set<Integer> setB = new ArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put Set A: ");
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setA.put(value) + ", " + setA);
            }
            count = 0;
            System.out.println("Put Set B: ");
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                System.out.println("Add: " + value + ", " + setB.put(value) + ", " + setB);
            }

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
            Set<Integer> setA = new ArraySet<>();
            Set<Integer> setB = new ArraySet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put Set A: ");
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setA.put(value) + ", " + setA);
            }
            count = 0;
            System.out.println("Put Set B: ");
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                System.out.println("Add: " + value + ", " + setB.put(value) + ", " + setB);
            }

            boolean result =  !setA.isSubsetOf(setB);
            System.out.println("Set A: " + setA);
            System.out.println("Set B: " + setB);
            System.out.println("Negative Subset Test: " + result);


            setB.clear();
            result = result && setB.isSubsetOf(setA);
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
