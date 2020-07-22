package algorithm.search.set;

import algorithm.search.RandomAssessable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * an ordered array based set implementation
 * binary search is used
 */
public class ArraySortedSet<E extends Comparable<E>> implements Set<E>, RandomAssessable<E, Integer> {
    protected int size;
    protected Comparable<?>[] elements;
    protected final int INITIAL_CAPACITY = 10;
    protected final int MAXIMUM_CAPACITY = Integer.MAX_VALUE - 8;

    public ArraySortedSet() {
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
        return false;
    }

    @Override
    public boolean contains(E element) {
        return false;
    }

    @Override
    public boolean remove(E element) {
        return false;
    }

    @Override
    public Set<E> union(Set<E> that) {
        return null;
    }

    @Override
    public Set<E> intersect(Set<E> that) {
        return null;
    }

    @Override
    public Set<E> complement(Set<E> that) {
        return null;
    }

    @Override
    public boolean isSubsetOf(Set<E> that) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
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
        return binarySearch(e, from, to - 1);
    }

    public void trim() {

    }

    @SuppressWarnings("unchecked")
    private int binarySearch(E element, int low, int high) {
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

}
