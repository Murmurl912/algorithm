package datastructure.container.impl;

import datastructure.container.Container;
import datastructure.container.Growable;
import datastructure.container.RandomAccessible;
import datastructure.container.structure.Vector;

import java.util.*;

public class ArrayVector<E> implements
        Vector<E>,
        RandomAccessible<E>,
        Growable {

    protected Object[] elements;
    protected int size;
    public static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE - 10;
    public static final int INITIAL_CAPACITY = 10;

    public ArrayVector() {
        init(INITIAL_CAPACITY);
    }

    public ArrayVector(int capacity) {
        if(capacity < 0) {
            capacity = INITIAL_CAPACITY;
        } else {
            init(capacity);
        }
    }

    public ArrayVector(Container<E> container) {
        Objects.requireNonNull(container);
        if(container.isEmpty()) {
            init(0);
        } else {
            init(container.size());
            for (E e : container) {
                elements[size++] = e;
            }
        }
    }

    private void init(int capacity) {
        elements = new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        ensureIndexInbound(index);
        return (E)elements[index];
    }

    @Override
    public void add(E data, int index) {
        if(index < 0 || index > size) {
            throw new IllegalArgumentException("index must between [0, " + size + "]");
        }
        if(size == elements.length) {
            grow(size + 1);
        }
        shift(index, index + 1);
        size++;
        elements[index] = data;
    }

    @Override
    public void replace(E data, int index) {
        ensureIndexInbound(index);
        elements[index] = data;
    }

    @Override
    public int replace(E target, E data, int from, int to) {
        ensureRangeInBound(from, to);
        for(int i = from; i < to; i++) {
            if(Objects.equals(target, elements[i])) {
                elements[i] = data;
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        ensureIndexInbound(index);
        Object data = elements[index];
        shift(index + 1, index);
        size--;
        return (E) data;
    }

    @Override
    public int remove(E data, int from, int to) {
        ensureRangeInBound(from, to);
        for (int i = from; i < to; i++) {
            if(Objects.equals(data, elements[i])) {
                shift(i + 1, i);
                size--;
                return i;
            }
        }
        return -1;
    }

    @Override
    public int search(E data, int from, int to) {
        ensureRangeInBound(from, to);
        for (int i = from; i < to; i++) {
            if(Objects.equals(data, elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void reverse() {
        Object temp = null;
        for (int i = 0; i < size / 2; i++) {
            temp = elements[i];
            elements[i] = elements[size - i - 1];
            elements[size - i - 1] = temp;
        }
    }

    private void reverseReduce() {
        Object temp = null;
        for(int i = 0, j = size - 1; i < j; i++, j--) {
            temp = elements[i];
            elements[j] = elements[i];
            elements[j] = temp;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for(;size > 0; size--) {
            elements[size - 1] = null;
        }
    }

    @Override
    public void sort(boolean isAscend, Comparator<E> comparator) {

    }

    @Override
    public Vector<E> slice(int from, int step, int to) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator(0);
    }

    @Override
    public E at(int index) {
        return get(index);
    }

    @Override
    public void grow(int min) {
        if(min < 0) {
            throw new OutOfMemoryError();
        }
        if(min < size) {
            throw new IllegalArgumentException("min: " + min + ", must larger than array size: " + size);
        }

        int capacity = (elements.length >> 1) + elements.length;
        if(capacity < min) {
            capacity = min;
        } else {
            if(capacity > MAXIMUM_CAPACITY) {
                if(min > MAXIMUM_CAPACITY) {
                    capacity = Integer.MAX_VALUE;
                } else {
                    capacity = min;
                }
            }
        }
        elements = Arrays.copyOf(elements, capacity);
    }

    @Override
    public void trim(int min) {
        if(min < size) {
            throw new IllegalArgumentException("min: " + min + ", must larger than array size: " + size);
        }

        elements = Arrays.copyOf(elements, min);
    }

    public int capacity() {
        return elements.length;
    }

    protected void ensureIndexInbound(int index) {
        if(index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(index);
        }
    }

    protected void ensureRangeInBound(int from, int to) {
        if(from < 0 || from > size) {
            throw new IllegalArgumentException("from must between [0, " + size + "]");
        }

        if(to < 0 || to > size) {
            throw new IllegalArgumentException("to must between [0, " + size + "]");
        }

        if(to < from) {
            throw new IllegalArgumentException("from must less or equal than to, from: " + from + ", to: " + to);
        }
    }

    protected void shift(int from, int to) {
        assert to - from + size <= elements.length;
        if(from == to) {
            return;
        }
        System.arraycopy(elements, from, elements, to, size - from);
        if(from > to) {
            int movedStep = from - to;
            for(int i = size -1; i > size - movedStep - 1; i--) {
                elements[i] = null;
            }
        }
    }

    private class ArrayIterator implements ListIterator<E> {
        private int cursor;
        private int lastReturned;

        public ArrayIterator(int index) {
            cursor = index;
            lastReturned = -1;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            E data = (E)elements[cursor];
            lastReturned = cursor++;
            return data;
        }

        @Override
        public void remove() {
            if(lastReturned < 0) {
                throw new IllegalStateException();
            }

            ArrayVector.this.remove(lastReturned);
            cursor = lastReturned;
                lastReturned--;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 1;
        }

        @Override
        public E previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return cursor + 1;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void set(E e) {
            if(lastReturned < 0) {
                throw new IllegalStateException();
            }
            ArrayVector.this.replace(e, lastReturned);
        }

        @Override
        public void add(E e) {
            ArrayVector.this.add(e, cursor);
        }
    }
}
