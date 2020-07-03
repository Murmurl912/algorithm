package datastructure.linear.sequential;

import java.util.*;

public class Array<T> implements List<T>, RandomAccess {
    protected Object[] elements;
    protected int elementCount = 0;
    private int initialCapacity = 10;
    private int capacityIncrement = 10;

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

    //todo: implement below methods
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

        // shift array

        // insert element

        // increase element count
    }

    /**
     * remove element at given position
     * @param index index of element to be remove
     * @return removed element at given index
     */
    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }


    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }



    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }


    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }


    private void ensureIndexInBounds(int index) {
        if(index < 0 || index > elementCount - 1) {
            throw new IndexOutOfBoundsException(index);
        }
    }
}
