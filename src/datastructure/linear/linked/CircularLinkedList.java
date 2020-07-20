package datastructure.linear.linked;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularLinkedList<E> implements Iterable<E> {

    protected Node<E> head;
    protected int size = 0;

    public void add(int index, E data) {
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException(index);
        }
        Node<E> current = node(index);
        Node<E> node = new Node<>(data, current, null);
        if(current == null) {
            // array is empty
            head = node;
            node.previous = node;
            node.next = node;
        } else {
            // add at start of array
            if(current == head && index == 0) {
                head = node;
            }
            node.previous = current.previous;
            current.previous.next = node;
        }
        // increase size
        size++;
    }

    public E get(int index) {
        if(index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(index);
        }
        Node<E> node = node(index);
        return node.data;
    }

    public E remove(int index) {
        if(index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(index);
        }
        Node<E> node = node(index);
        E data = node.data;

        if(size == 1) {
            // only one node
            head = null;
        } else {
            Node<E> previous = node.previous;
            Node<E> next = node.next;
            previous.next = next;
            next.previous = previous;
            // if delete node is head node
            if(node == head) {
                head = next;
            }
        }
        // garbage collect
        node.previous = null;
        node.next = null;
        node.data = null;
        // decrease size
        size--;
        return data;
    }

    private Node<E> node(int index) {
        Node<E> node = head;
        int counter = 0;
        while (counter++ < index) {
            node = node.next;
        }

        return node;
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularLinkedListIterator();
    }

    private class CircularLinkedListIterator implements Iterator<E> {
        private Node<E> node = head;
        private int index = 0;
        @Override
        public boolean hasNext() {
            return index < size && node != null;
        }

        @Override
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            E data = node.data;
            node = node.next;
            index++;
            return data;
        }

        @Override
        public void remove() {
            if(index < 1) {
                throw new IllegalStateException();
            }

            Node<E> lastReturned = node.previous;
            if(lastReturned == node) {
                // array contain only on element
                head = null;
                node = null;
            } else {
                // delete previous node
                Node<E> previous = lastReturned.previous;
                Node<E> next = node;
                previous.next = next;
                next.previous = previous;
                if(lastReturned == head) {
                    head = next;
                }
            }

            // garbage collect
            lastReturned.previous = null;
            lastReturned.next = null;
            lastReturned.data = null;
            // decrease size and index
            size--;
            index--;
        }
    }

    private static class Node<E> {
        private E data;
        private Node<E> next;
        private Node<E> previous;

        public Node() {

        }

        public Node(E data, Node<E> next, Node<E> previous) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }

        public E getData() {
            return data;
        }

        public void setData(E data) {
            this.data = data;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }

        public Node<E> getPrevious() {
            return previous;
        }

        public void setPrevious(Node<E> previous) {
            this.previous = previous;
        }
    }
}
