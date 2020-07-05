package datastructure.linear.sequential;

import datastructure.linear.BasicArray;

import java.util.*;
import java.util.function.Consumer;

public class SequentialArray<E> implements BasicArray<E> {
    protected Object[] elements;
    protected int size = 0;
    public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 10;

    public SequentialArray() {
        elements = new Object[10];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }

        // out of capacity
        if(elements.length == size) {
            grow(size + 1);
        }

        // moved size
        // move elements in between [index] and [size - 1] forwards
        // size to move: size - 1 - index + 1
        int moveSize = size - index;
        if(moveSize > 0) {
            // shift array forwards
            System.arraycopy(elements, index, elements, index + 1, moveSize);
        }

        // put element and increase size
        elements[index] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        ensureInBounds(index);
        return (E)elements[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        ensureInBounds(index);
        E element = (E)elements[index];
        // move size
        // move elements in between [index + 1] and [size -1] backwards
        // move size: [size - 1] - [index + 1] + 1
        int moveSize = size - index - 1;
        if(moveSize > 0) {
            // shift array backwards
            System.arraycopy(elements, index + 1, elements, index, moveSize);
        }

        // gc and decrease size
        elements[size] = null;
        size--;
        return element;
    }

    @Override
    public int remove(E element) {
        // search index
        int index = first(0, element);
        // remove if index in bounds
        if(index > -1 && index < size) {
            remove(index);
        }
        return index;
    }

    @Override
    public int first(int start, E element) {
        ensureInBounds(start);
        // forward search
        for(int i = start; i < size; i++) {
            if(Objects.equals(element, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int last(int end, E element) {
        ensureInBounds(end);
        // backward search
        for(int i = end; i > -1; i--) {
            if(Objects.equals(element, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void clear() {
        while (--size > -1) {
            elements[size] = null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        ensureInBounds(index);
        E old = (E)elements[index];
        elements[index] = element;
        return old;

    }

    @Override
    public Iterator<E> iterator() {
        return new SequentialListIterator();
    }

    @Override
    public Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }

    public ListIterator<E> listIterator(int index) {
        ensureInBounds(index);
        return new SequentialListIterator(index);
    }

    private void ensureInBounds(int index) {
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
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
            if(desireCapacity > MAX_ARRAY_SIZE) {
                // mini capacity larger than maximum array size
                // but mini capacity can never larger than Integer.MAX_VALUE
                if(miniCapacity > MAX_ARRAY_SIZE) {
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

    private class SequentialListIterator implements ListIterator<E> {
        private int cursor = 0;

        public SequentialListIterator() {

        }

        public SequentialListIterator(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor < size && cursor > -1;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if(cursor > -1 && cursor < size) {
                return (E)elements[cursor++];
            }
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0 && cursor < size + 1;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E previous() {
            if(cursor > 0 && cursor < size + 1) {
                return (E)elements[--cursor];
            }
            return null;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if(cursor > 0 && cursor < size + 1) {
                SequentialArray.this.remove(--cursor);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            while (cursor < size) {
                action.accept((E)elements[cursor++]);
            }
        }

        @Override
        public void set(E e) {
            if(cursor > 0 && cursor < size + 1) {
                elements[cursor - 1] = e;
            }
        }

        @Override
        public void add(E e) {
            if(cursor > -1 && cursor < size + 1) {
                SequentialArray.this.add(cursor++, e);
            }
        }


    }

    private static class Test {
        public static void main(String[] args) {
            SequentialArray<Integer> sequentialArray = new SequentialArray<>();
        }

        static void testAdd() {

        }

        static void testRemove() {

        }

        static void testSet() {

        }

        static void testClear() {

        }
    }
}
