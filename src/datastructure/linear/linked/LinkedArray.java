package datastructure.linear.linked;

import datastructure.linear.BasicArray;

import java.util.*;
import java.util.function.Consumer;

public class LinkedArray<E> implements BasicArray<E> {

    protected LinkedNode<E> first;
    protected LinkedNode<E> last;
    protected int size;

    public LinkedArray() {

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

        if(index == size) {
            // add at end of array
            LinkedNode<E> previous = last;
            LinkedNode<E> node = new LinkedNode<>(
                    previous, // previous node
                    null,   // next node
                    element);

            if(previous == null) {
                // previous node is null
                // list is empty
                first = node;
            } else {
                // link previous to next
                previous.next = node;
            }
        } else {
            LinkedNode<E> next = node(index);
            LinkedNode<E> previous = next.previous;
            LinkedNode<E> node = new LinkedNode<>(
                    previous, // previous node
                    next,   // next node
                    element);
            if(previous == null) {
                // previous node is null
                // add at start of array
                first = node;
            } else {
                // link previous
                previous.next = node;
                // link next
                next.previous = node;
            }
        }
        size++;
    }

    @Override
    public E get(int index) {
        ensureIndexInBounds(index);
        return node(index).data;
    }

    @Override
    public E remove(int index) {
        ensureIndexInBounds(index);
        LinkedNode<E> node = node(index);
        return remove(node);
    }

    @Override
    public int remove(E element) {
        int index = -1;
        for(LinkedNode<E> node = first; node != null; node = node.next) {
            index++;
            if(Objects.equals(node.data, element)) {
                remove(node);
                return index;
            }
        }
        return -1;
    }

    @Override
    public int first(int start, E element) {
        ensureIndexInBounds(start);
        int counter = 0;
        LinkedNode<E> node = node(start);
        while (node != null) {
            if(Objects.equals(node.data, element)) {
                return counter;
            }
            node = node.next;
            counter++;
        }
        return -1;
    }

    @Override
    public int last(int end, E element) {
        ensureIndexInBounds(end);
        int counter = 0;
        LinkedNode<E> node = node(end);
        while (node != null) {
            if(Objects.equals(node.data, element)) {
                return counter;
            }
            node = node.previous;
            counter++;
        }
        return -1;
    }

    @Override
    public void clear() {
        while (first != null) {
            first.data = null;
            first.previous = null;
            first = first.next;
            size--;
        }
    }

    @Override
    public E set(int index, E element) {
        ensureIndexInBounds(index);
        LinkedNode<E> node = node(index);
        E old = node.data;
        node.data = element;
        return old;
    }

    @Override
    public ListIterator<E> iterator() {
        return new LinkedArrayIterator(0);
    }

    private class LinkedArrayIterator implements ListIterator<E> {
        private LinkedNode<E> lastReturned;
        private LinkedNode<E> next;
        private int nextIndex;

        LinkedArrayIterator(int index) {
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        public boolean hasNext() {
            return nextIndex < size;
        }

        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.data;
        }

        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        public E previous() {
            if (!hasPrevious())
                throw new NoSuchElementException();

            lastReturned = next = (next == null) ? last : next.previous;
            nextIndex--;
            return lastReturned.data;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();

            LinkedNode<E> lastNext = lastReturned.next;
//            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
        }

        public void set(E e) {
            if (lastReturned == null)
                throw new IllegalStateException();
            lastReturned.data = e;
        }

        public void add(E e) {
            lastReturned = null;
            if (next == null) {

            } else {

            }
            nextIndex++;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            while (nextIndex < size) {
                action.accept(next.data);
                lastReturned = next;
                next = next.next;
                nextIndex++;
            }
        }
    }

    private void ensureIndexInBounds(int index) {
        // index bound [0,..., size - 1]
        if(index > size - 1 || index < 0) {
            throw new IndexOutOfBoundsException(index);
        }
    }

    private E remove(LinkedNode<E> node) {

        LinkedNode<E> previous = node.previous;
        LinkedNode<E> next = node.next;
        if(previous == null) {
            // node is first node
            first = next;
        } else {
            previous.next = next;
        }

        if(next == null) {
            // node is last node
            last = previous;
        } else {
            next.previous = previous;
        }

        node.previous = null;
        node.next = null;
        E data = node.data;
        node.data = null;
        size--;
        return data;
    }

    private LinkedNode<E> node(int index) {
        if (index < (size >> 1)) {
            LinkedNode<E> node = first;
            for (int i = 0; i < index; i++)
                node = node.next;
            return node;
        } else {
            LinkedNode<E> node = last;
            for (int i = size - 1; i > index; i--)
                node = node.previous;
            return node;
        }
    }

    private static class LinkedNode<E> {
        private LinkedNode<E> previous;
        private LinkedNode<E> next;
        private E data;

        public LinkedNode() {

        }

        public LinkedNode(LinkedNode<E> previous,
                          LinkedNode<E> next,
                          E data) {
            this.data = data;
            this.previous = previous;
            this.next = next;
        }

        public LinkedNode<E> getPrevious() {
            return previous;
        }

        public void setPrevious(LinkedNode<E> previous) {
            this.previous = previous;
        }

        public LinkedNode<E> getNext() {
            return next;
        }

        public void setNext(LinkedNode<E> next) {
            this.next = next;
        }

        public E getData() {
            return data;
        }

        public void setData(E data) {
            this.data = data;
        }
    }
}
