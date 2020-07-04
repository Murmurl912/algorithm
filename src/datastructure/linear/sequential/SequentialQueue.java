package datastructure.linear.sequential;

import datastructure.linear.BasicQueue;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;

public class SequentialQueue<E> implements BasicQueue<E> {

    protected Object[] elements;
    protected int size;
    public static final int MAX_QUEUE_SIZE = Integer.MAX_VALUE - 10;
    private transient volatile int front;
    private transient volatile int rear;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        while (--size > 0) {
            elements[size] = null;
        }
    }

    @Override
    public E take() {
        return null;
    }

    @Override
    public void offer(E element) {
        if(size == elements.length) {
            grow(size + 1);
        }

    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
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
            if(desireCapacity > MAX_QUEUE_SIZE) {
                // mini capacity larger than maximum array size
                // but mini capacity can never larger than Integer.MAX_VALUE
                if(miniCapacity > MAX_QUEUE_SIZE) {
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

}
