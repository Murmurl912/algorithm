package datastructure.linear.sequential;

import java.util.*;
import java.util.function.Consumer;

/**
 * This is a bad choice to implement List
 * @param <T>
 */
@Deprecated
public class Array<T> implements List<T>, RandomAccess {
    protected Object[] elements;
    protected int elementCount = 0;
    private int initialCapacity = 10;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 10;

/*
==============================================================================================
                            Constructor
==============================================================================================
 */

    public Array() {
        elements = new Object[initialCapacity];
    }



/*
===============================================================================================
                            Empty and Size
===============================================================================================
 */

    /**'
     * return size of element stored in array
     * @return size of element in array
     */
    @Override
    public synchronized int size() {
        return elementCount;
    }

    /**
     * return if array is empty
     * this is equivalent to size() == 0
     * @return whether there is element stored in array
     */
    @Override
    public synchronized boolean isEmpty() {
        return elementCount == 0;
    }

/*
===============================================================================================
                            Search and Index
===============================================================================================
 */

    /**
     * get element in specified position of array by index
     * @param index index of element to return
     * @return element at specified index of array
     * @throws ArrayIndexOutOfBoundsException when index out of bounds
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized T get(int index) {
        ensureIndexInBounds(index);
        return (T)elements[index];
    }

    /**
     * check if array contain the specified element
     * @param o element to be tested whether it exists in array
     * @return true if element in the array
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) > -1;
    }

    /**
     * get first appearance's index in array of given element
     * @param o element to search for
     * @return index of element first appearance if element exists in array,
     * otherwise -1 is returned
     */
    @Override
    public int indexOf(Object o) {
        return indexOf(o, 0);
    }

    /**
     * get the index of the first occurrence of the specified element
     * start at given index.
     * @param o element to search for
     * @param index index to start search from
     * @return index of first occurrence of element start at given index,
     * -1 if element is not found.
     * @throws IndexOutOfBoundsException if given index is out of bounds of this array
     */
    public synchronized int indexOf(Object o, int index) {
        ensureIndexInBounds(index);
        for (int i = index ; i < elementCount ; i++) {
            if (Objects.equals(elements[i], o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * get index of last occurrence of specified element
     * @param o element to search for
     * @return index of last occurrence of the specified element
     * if element is not found -1 will be returned
     */
    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOf(o, elementCount - 1);
    }

    /**
     * get index of last occurrence of given element before index
     * in this array
     * @param o element to search from
     * @param index index where to start backwards search
     * @return index of last occurrence of given element before index,
     * -1 if element is not found
     */
    public synchronized int lastIndexOf(Object o, int index) {
        ensureIndexInBounds(index);
        for (int i = index; i >= 0; i--) {
            if (Objects.equals(elements[i], o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * test if this array contain all elements specified in a collection
     * @param c elements to be tested for containment stored in this collection
     * @return true if array contain all elements in given collection
     */
    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        for(Object element : c) {
            if(!contains(c)) {
                return false;
            }
        }
        return true;
    }


/*
===============================================================================================
                            Add, remove and update
===============================================================================================
 */

    /**
     * clear elements in array
     * the capacity of array will not change
     */
    @Override
    public synchronized void clear() {
        for (int i = 0; i < elementCount; i++) {
            elements[i] = null;
        }
        elementCount = 0;
    }

    /**
     * replace element in given position of given index
     * @param index index of element to be replaced
     * @param element element to be stored at given position
     * @return the element previously at the specified position
     * @throws ArrayIndexOutOfBoundsException if given index is out of bounds
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized T set(int index, T element) {
        ensureIndexInBounds(index);
        T old = (T) elements[index];
        elements[index] = element;
        return old;
    }

    /**
     * insert element at given index, previously element at index
     * will be shifted forwards.
     * @param index where to insert element
     * @param element element to be insert
     */
    @Override
    public synchronized void add(int index, T element) {
        // bounds check
        if(index < 0 || index > elementCount) {
            throw new IndexOutOfBoundsException(index);
        }

        // ensure capacity
        if(elements.length == elementCount) {
            grow(elements.length + 1);
        }
        // shift array
        System.arraycopy(elements, index, elements, index + 1, elementCount - index);

        // insert element
        elements[index] = element;
        // increase element count
        elementCount++;
    }

    /**
     * remove element at given position
     * @param index index of element to be remove
     * @return removed element at given index
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized T remove(int index) {
        ensureIndexInBounds(index);
        T old = (T)elements[index];
        // return last element
        if(index == elementCount - 1) {
            elements[index] = null;
            elementCount--;
            return old;
        }
        // [0, ..., a, index, b, ..., count - 1] => [0, ..., a, b, ..., count - 2, count - 1]
        System.arraycopy(elements, index + 1, elements, index, elementCount - index - 1);
        // gc elements[elementCount]
        elements[elementCount] = null;
        // decrease element count
        elementCount--;
        return old;
    }

    /**
     * add element at end of array
     * @param t element to be add
     * @return always true
     */
    @Override
    public synchronized boolean add(T t) {
        if(elementCount == elements.length) {
            grow(1);
        }
        elements[elementCount] = t;
        elementCount++;
        return true;
    }

    /**
     * remove the first occurrence of the specified element
     * @param o element to be removed
     * @return return true if element exist in array
     */
    @Override
    public synchronized boolean remove(Object o) {
        int index = indexOf(o);
        if(index < 0) {
            return false;
        }
        remove(index);
        return true;
    }

    /**
     * add every element in collection to this array
     * @param c collection that stores the elements to be add
     * @return false if collection is empty
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        int size = c.size();
        if(size == 0) {
            return false;
        }
        if(elementCount + size > elements.length) {
            grow(size);
        }
        Object[] array = c.toArray();
        // add to elements
        System.arraycopy(array, 0, elements, elementCount, size);
        // increase element count
        elementCount += size;
        return true;
    }

    /**
     * insert every element in collection into array
     * the first element is inserted in index, the second is inserted in index + 1 etc.
     * at at give start position
     * @param index where to start insert
     * @param c elements to insert
     * @return false if collection is emptu
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if(index < 0 || index > elementCount) {
            throw new IndexOutOfBoundsException(index);
        }

        int size = c.size();
        if(size == 0) {
            return false;
        }

        if(elementCount + size > elements.length) {
            grow(size);
        }

        Object[] array = c.toArray();
        // append at end of array
        if(index == elementCount) {
            System.arraycopy(array, 0, elements, elementCount, size);
            elementCount += size;
            return true;
        }
        // shift element forwards
        System.arraycopy(elements, index, elements, index + size, elementCount - index);
        // add to elements
        System.arraycopy(array, 0, elements, index, size);
        elementCount += size;
        return true;
    }

    /**
     * todo rewrite
     * remove every single element in c from array
     * @param c elements to be remove
     * @return true if array change as result of this call
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if(c.isEmpty()) {
            return false;
        }
        boolean flag = false;
        for (Object o : c) {
            while (remove(o)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * todo rewrite
     * remove every element not in given collection
     * @param c element to retain
     * @return true if array change as result of this call
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        if(c.isEmpty()) {
            return false;
        }

        boolean flag = false;
        for(int i = 0; i < elementCount; i++) {
            Object element = elements[i];
            if(!c.contains(element)) {
                flag = true;
                if(i != elementCount - 1) {
                    System.arraycopy(elements, i + 1, elements, i, elementCount - i);
                }
                // gc
                elements[elementCount - 1] = null;
                elementCount--;
            }
        }

        return flag;
    }


/*
===============================================================================================
                            Iterator
===============================================================================================
 */

    // todo implement iterator

    @Override
    public synchronized ListIterator<T> listIterator() {
        return new ListArrayIterator();
    }

    @Override
    public synchronized ListIterator<T> listIterator(int index) {
        ensureIndexInBounds(index);
        return new ListArrayIterator(index);
    }

    @Override
    public synchronized List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("This implementation does not supported sublist");
    }


    @Override
    public synchronized Iterator<T> iterator() {
        return new ArrayIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, elementCount);
    }

    /**
     * copy to an array
     * @param a where to store the copied array
     * @param <E> type of element
     * @return copied array stored in a if given array has enough size
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized <E> E[] toArray(E[] a) {
        if (a.length < elementCount)
            return (E[]) Arrays.copyOf(elements, elementCount, a.getClass());

        System.arraycopy(elements, 0, a, 0, elementCount);

        if (a.length > elementCount)
            a[elementCount] = null;

        return a;
    }

/*
===============================================================================================
                            private method
===============================================================================================
 */

    private void ensureIndexInBounds(int index) {
        if(index < 0 || index > elementCount - 1) {
            throw new IndexOutOfBoundsException(index);
        }
    }

    /**
     * This method is adopted from ArrayList.java
     * Returns a capacity at least as large as the given minimum capacity.
     * Returns the current capacity increased by 50% if that suffices.
     * Will not return a capacity greater than MAX_ARRAY_SIZE unless
     * the given minimum capacity is greater than MAX_ARRAY_SIZE.
     *
     * @param minCapacity the desired minimum capacity
     * @throws OutOfMemoryError if minCapacity is less than zero
     */
    private int newCapacity(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elements.length;
        // increase size by 1.5 times
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        // this line is equal to newCapacity <= minCapacity
        // when new capacity is less than min capacity
        // grow as much as min capacity
        if (newCapacity - minCapacity <= 0) {
            if (minCapacity < 0) // overflow
                throw new OutOfMemoryError();
            return minCapacity;
        }

        // when new capacity is lager than max size
        // reduce capacity to min capacity
        // otherwise gow as little as new capcity
        return (newCapacity - MAX_ARRAY_SIZE <= 0)
                ? newCapacity
                : hugeCapacity(minCapacity);
    }

    /*
     * This method is adopted from ArrayList.java
     */
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE)
                ? Integer.MAX_VALUE
                : MAX_ARRAY_SIZE;
    }


    public synchronized void grow(int size) {
        elements = Arrays.copyOf(elements, newCapacity(elements.length + size));
    }

/*
===============================================================================================
                            private class
===============================================================================================
 */

    /**
     * This class does not provide ConcurrentModificationException check
     */
    private class ArrayIterator implements Iterator<T> {

        private int cursor = 0;

        @Override
        public boolean hasNext() {
            synchronized (Array.this) {
                return cursor < elementCount;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            synchronized (Array.this) {
                if(cursor < elementCount) {
                    return (T) elements[cursor++];
                }
                return null;
            }
        }

        @Override
        public void remove() {
            synchronized (Array.this) {
                if(cursor < elementCount) {
                    Array.this.remove(cursor - 1);
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            synchronized (Array.this) {
                int i;
                for(i = cursor; cursor < elementCount; i++) {
                    action.accept((T)elements[i]);
                }
                cursor = i;
            }
        }
    }

    private class ListArrayIterator implements ListIterator<T> {
        private int cursor = 0;

        public ListArrayIterator() {

        }

        public ListArrayIterator(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            synchronized (Array.this) {
                return cursor < elementCount;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            synchronized (Array.this) {
                if(cursor < elementCount) {
                    return (T)elements[cursor];
                } else {
                    return null;
                }
            }
        }

        @Override
        public boolean hasPrevious() {
            synchronized (Array.this) {
                return cursor > 0;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public T previous() {
            synchronized (Array.this) {
                int index = cursor - 1;
                if(index > 0) {
                    cursor = index;
                    return (T)elements[index];
                } else {
                    return null;
                }
            }
        }

        @Override
        public int nextIndex() {
            synchronized (Array.this) {
                return cursor;
            }
        }

        @Override
        public int previousIndex() {
            synchronized (Array.this) {
                return cursor - 1;
            }
        }

        @Override
        public void remove() {
            synchronized (Array.this) {
                if(cursor < elementCount) {
                    Array.this.remove(cursor - 1);
                }
            }
        }

        @Override
        public void set(T t) {
            synchronized (Array.this) {
                if(cursor < elementCount && cursor > -1) {
                    elements[cursor] = t;
                }
            }
        }

        @Override
        public void add(T t) {
            synchronized (Array.this) {
                Array.this.add(cursor, t);
                cursor++;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            synchronized (Array.this) {
                int i;
                for(i = cursor; cursor < elementCount; i++) {
                    action.accept((T)elements[i]);
                }
                cursor = i;
            }
        }
    }

    private static class ArrayTest {
        public static void main(String[] args) {
            testAdd(0, 2000);
            testAddAll(0, 100, 2000);
            testRemove(2000, 1000);
        }


        public static void testAdd(int arrayInitialSize, int times) {
            Array<Integer> array = new Array<>();
            Random random = new Random();
            random.setSeed(0);
            for (int i = 0; i < arrayInitialSize; i++) {
                int number = random.nextInt();
                array.add(number);
            }
            random.setSeed(0);
            for(int i = 0; i < times; i++) {
                int index = Math.abs(random.nextInt(array.size() + 1));
                int number = random.nextInt();
                int[] arrayCorrect = new int[array.size() + 1];
                for (int j = 0; j < index; j++) {
                    arrayCorrect[j] = array.get(j);
                }
                arrayCorrect[index] = number;
                for (int j = index; j < array.size(); j++) {
                    arrayCorrect[j + 1] = array.get(j);
                }

                array.add(index, number);
                for (int j = 0; j < array.size(); j++) {
                    if(array.get(j) != arrayCorrect[j]) {
                        System.out.println("Test failed");
                        return;
                    }
                }
            }
            System.out.println("Test success");
        }

        public static void testAddAll(int arrayInitialSize, int maxSize, int times) {
            Array<Integer> array = new Array<>();
            Random random = new Random();
            random.setSeed(0);
            for (int i = 0; i < arrayInitialSize; i++) {
                int number = random.nextInt();
                array.add(number);
            }
            random.setSeed(0);
            for(int i = 0; i < times; i++) {
                int index = Math.abs(random.nextInt(array.size() + 1));
                int size = Math.abs(random.nextInt(maxSize));
                int[] arrayCorrect = new int[array.size() + size];
                for (int j = 0; j < index; j++) {
                    arrayCorrect[j] = array.get(j);
                }
                List<Integer> testCollection = new ArrayList<>();
                for(int j = index; j < index + size; j++) {
                    arrayCorrect[j] = random.nextInt();
                    testCollection.add(arrayCorrect[j]);
                }
                for (int j = index; j < array.size(); j++) {
                    arrayCorrect[j + size] = array.get(j);
                }
                array.addAll(index, testCollection);
                for (int j = 0; j < array.size(); j++) {
                    if(array.get(j) != arrayCorrect[j]) {
                        System.out.println("Test failed");
                        return;
                    }
                }
            }
            System.out.println("Test success");
        }

        public static void testRemove(int arrayInitialSize, int times) {
            Array<Integer> array = new Array<>();
            Random random = new Random();
            random.setSeed(0);
            for (int i = 0; i < arrayInitialSize; i++) {
                int number = random.nextInt();
                array.add(number);
            }
            random.setSeed(0);
            for(int i = 0; i < times; i++) {
                int index = Math.abs(random.nextInt(array.size()));
                int[] arrayCorrect = new int[array.size() - 1];
                for (int j = 0; j < index; j++) {
                    arrayCorrect[j] = array.get(j);
                }
                for(int j = index; j < arrayCorrect.length; j++) {
                    arrayCorrect[j] = array.get(j + 1);
                }
                array.remove(index);
                for (int j = 0; j < array.size(); j++) {
                    if(array.get(j) != arrayCorrect[j]) {
                        System.out.println("Test failed");
                        return;
                    }
                }
            }
            System.out.println("Test success");
        }

        public void testContain(int arrayInitialSize, int times) {

        }
    }
}
