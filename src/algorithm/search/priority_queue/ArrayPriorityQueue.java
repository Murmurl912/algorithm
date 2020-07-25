package algorithm.search.priority_queue;

import datastructure.linear.BasicQueue;

import javax.swing.*;
import java.util.*;

/**
 * an unordered array implementation of priority queue
 *
 * time complexity of different implementations
 * n is the number queue size
 * data structure       insert      delete maximum
 * ordered array        O(n)           O(1)
 * unordered array      O(1)           O(N)
 * heap                 O(logn)        O(logn)
 *
 * ordered array is an active solution, it keeps queue ordered when new
 * element is being added to queue which lowing operation more easier
 * and effective
 *
 * while unordered array is a lazy approach, it only take action wnecessary.
 *
 *
 * @param <E>
 */
public class ArrayPriorityQueue<E> implements BasicQueue<E> {

    protected int size;
    protected Object[] elements;
    protected final int INITIAL_CAPACITY = 10;
    protected static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE;
    protected final Comparator<E> comparator;

    public ArrayPriorityQueue(Comparator<E> comparator) {
        elements = new Object[INITIAL_CAPACITY];
        this.comparator = comparator;
    }

    /**
     * return how many total element
     * this priority queue can store with
     * grow underlying array
     * @return number of maximum element can stored in this queue
     *         without grow array
     */
    public int capacity() {
        return elements.length;
    }

    /**
     * trim capacity to element size
     */
    public void trim() {
        elements = Arrays.copyOf(elements, size);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * remove all elements in this queue
     */
    @Override
    public void clear() {
        for(int i = size; i > 0; i++, size--) {
            elements[i - 1] = null;
        }
    }

    /**
     * take the maximum element
     * in this queue
     * @return maximum priority element
     */
    @SuppressWarnings("unchecked")
    @Override
    public E take() {
        int index = search();
        if(index < 0) {
            throw new NoSuchElementException("queue is empty");
        }
        E element = (E)elements[index];
        remove(index);
        return element;
    }

    @Override
    public void offer(E element) {
        Objects.requireNonNull(element);
        insert(size, element);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        int index = search();
        if(index < 0) {
            throw new NoSuchElementException("queue is empty");
        }

        return (E)elements[index];
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @Override
    public boolean full() {
        return size == elements.length;
    }

    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    private int search() {
        // queue is empty
        if(size == 0) {
            return -1;
        }
        Object maxElement = elements[0];
        int index = 0;
        for (int i = 1; i < size; i++) {
            if(compare(maxElement, elements[i]) < 0) {
                // candidate max element < current element
                maxElement = elements[i];
                index = i;
            }
        }
        return index;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        return comparator.compare((E)a, (E)b);
    }

    private void remove(int index) {
        if(size == 0) {
            return;
        }

        int moveSize = size - index - 1;
        if(moveSize > 0) {
            System.arraycopy(
                    elements,
                    index + 1,
                    elements,
                    index,
                    moveSize
            );
        }
        elements[--size] = null;
    }

    private void insert(int index, E element) {
        if(size == elements.length) {
            grow(size + 1);
        }

        int moveSize = elements.length - index;
        if(moveSize > 0) {
            System.arraycopy(
                    elements,
                    index,
                    elements,
                    index + 1,
                    moveSize);
        }
        size++;
        elements[index] = element;
    }

    private void grow(int miniCapacity) {
        if(miniCapacity < 0) {
            // int overflow
            throw new OutOfMemoryError();
        }

        int capacity = (miniCapacity >> 1) + miniCapacity;
        if(capacity <= miniCapacity) {
            capacity = miniCapacity;
        }

        elements = Arrays.copyOf(elements, capacity);
    }

    private class QueueIterator implements Iterator<E> {
        int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if(index >= size) {
                throw new NoSuchElementException();
            }
            return (E)elements[index++];
        }

        @Override
        public void remove() {
            if(index < 1) {
                throw new NoSuchElementException();
            }
            ArrayPriorityQueue.this.remove(--index);
        }
    }
}
