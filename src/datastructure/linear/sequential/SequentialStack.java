package datastructure.linear.sequential;

import datastructure.linear.BasicStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

public class SequentialStack<E> implements BasicStack<E> {

    protected Object[] elements;
    protected int size = 0;
    public static final int MAX_STACK_SIZE = Integer.MAX_VALUE - 10;

    public SequentialStack() {
        elements = new Object[10];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        while (--size > -1) {
            elements[size] = null;
        }
        size = 0;
    }

    @Override
    public void push(E element) {
        if(size == elements.length) {
            grow(size + 1);
        }
        elements[size] = element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E pop() {
        if(size > 0) {
            return (E)elements[--size];
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        if(size > 0) {
            return (E) elements[size - 1];
        }
        return null;
    }

    /**
     * return an iterator that iterates elements from bottom to top
     * of the stack
     * if there is no element left in stack, iterator#next return null
     * @return iterator iterate element in this stack form bottom to top
     */
    @Override
    public Iterator<E> iterator() {
        return new StackIterator();
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
            if(desireCapacity > MAX_STACK_SIZE) {
                // mini capacity larger than maximum array size
                // but mini capacity can never larger than Integer.MAX_VALUE
                if(miniCapacity > MAX_STACK_SIZE) {
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

    private class StackIterator implements Iterator<E> {
        private int cursor = 0;
        @Override
        public boolean hasNext() {
            return cursor > -1 && cursor < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if(cursor > -1 && cursor < size) {
                return (E)elements[cursor++];
            }
            return null;
        }

    }

    private static class Test {
        public static void main(String[] args) {

        }
    }
}
