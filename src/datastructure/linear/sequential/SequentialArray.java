package datastructure.linear.sequential;

import datastructure.linear.BasicArray;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public class SequentialArray<E> implements BasicArray<E> {
    protected Object[] elements;
    protected int size;
    public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 10;

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
        while (--size > 0) {
            elements[size] = null;
        }
    }

    @Override
    public void set(int index, E element) {

    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super E> action) {

    }

    @Override
    public Spliterator<E> spliterator() {
        return null;
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
}
