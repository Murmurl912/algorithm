package datastructure.linear.linked;

import datastructure.linear.BasicArray;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

/**
 * A single linked list with head
 * implement with reverse
 * reverse index
 * @param <E>
 */
public class SingleLinkedList<E> implements BasicArray<E> {

    protected int size = 0;
    protected LinkedNode<E> head;

    public SingleLinkedList() {
        head = new LinkedNode<>(null, null);
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

        LinkedNode<E> node = head.next;
        LinkedNode<E> previous = null;
        int counter = 0;
        while (counter < index && node != null) {
            previous = node;
            node = node.next;
            counter++;
        }
        LinkedNode<E> newNode = new LinkedNode<>(node, element);
        if(previous == null) {
            // first node
            head.next = newNode;
        } else {
            previous.next = newNode;
        }
        size++;
    }

    @Override
    public E get(int index) {
        if(index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(index);
        }
        LinkedNode<E> node = head.next;
        int counter = 0;
        while (counter < index) {
            counter++;
            node = node.next;
        }

        return node.data;
    }

    @Override
    public E remove(int index) {
        if(index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(index);
        }
        LinkedNode<E> node = head.next;
        LinkedNode<E> previous = null;
        int counter = 0;
        while(counter < index) {
            counter++;
            previous = node;
            node = node.next;
        }

        if(previous == null) {
            head.next = node.next;
        } else {
            previous.next = node.next;
        }
        E data = node.data;
        node.next = null;
        node.data = null;
        size--;
        return data;
    }

    @Override
    public int remove(E element) {
        LinkedNode<E> node = head.next;
        LinkedNode<E> previous = null;
        int counter = 0;
        while (node != null && !Objects.equals(node.data, element)) {
            previous = node;
            node = node.next;
            counter++;
        }
        if(node == null){
            return -1;
        }

        if(previous == null) {
            // head
            head.next = node.next;
        } else {
            previous.next = node.next;
        }
        node.next = null;
        node.data = null;
        size--;
        return counter;
    }

    @Override
    public int first(int start, E element) {
        if(start < 0 || start > size - 1) {
            throw new IndexOutOfBoundsException(start);
        }

        int counter = 0;
        LinkedNode<E> node = head.next;
        while (counter++ < start) {
            node = node.next;
        }
        while (node != null && !Objects.equals(node.data, element)) {
            counter++;
            node = node.next;
        }

        return counter > size - 1 ? -1 : counter;
    }

    @Override
    public int last(int end, E element) {
        throw
                new UnsupportedOperationException(
                        "int last(int end, E element) is not supported in single linked list");
    }

    @Override
    public void clear() {
        LinkedNode<E> node = head.next;
        while (node != null) {
            head.next = node.next;
            node.next = null;
            node.data = null;
            node = head.next;
        }
    }

    @Override
    public E set(int index, E element) {
        if(index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(index);
        }
        LinkedNode<E> node = head.next;
        int counter = 0;
        while(counter < index) {
            counter++;
            node = node.next;
        }
        E old = node.data;
        node.data = element;
        return old;
    }

    @Override
    public Iterator<E> iterator() {
        return new SingleLinkedIterator();
    }

    private class SingleLinkedIterator implements Iterator<E> {
        LinkedNode<E> previousPredecessor;
        LinkedNode<E> previous;
        LinkedNode<E> node;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            previousPredecessor = previous;
            previous = node;
            node = node.next;
            return previous.data;
        }
    }

    private static class LinkedNode<T> {
        private LinkedNode<T> next;
        private T data;

        public LinkedNode() {

        }

        public LinkedNode(LinkedNode<T> next, T data) {
            this.next = next;
            this.data = data;
        }

        public LinkedNode<T> getNext() {
            return next;
        }

        public void setNext(LinkedNode<T> next) {
            this.next = next;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    /**
     * return number of nodes in single linked node with head
     * @return number of nodes
     */
    public int count() {
        int counter = 0;
        LinkedNode<E> node = head.next;
        while (node != null) {
            node = node.next;
            counter++;
        }
        return counter;
    }

    /**
     * find last node in backward
     * @param index last node index
     * @return data store in last node if not found return null
     */
    @SuppressWarnings("unchecked")
    public E last(int index) {
        LinkedNode<E> node = head.next;
        int front = 0;
        int rear = 0;
        Object[] objects = new Object[index + 2];
        while (node != null) {
            int size = rear - front;
            if (size < 0) {
                size += objects.length;
            }
            // queue size = index
            if (size == index + 1) {
                objects[front] = null;
                front++;
                if (front == objects.length) {
                    front = 0;
                }
            }

            objects[rear++] = node.data;
            if (rear == objects.length) {
                rear = 0;
            }
            node = node.next;
        }

        int size = rear - front;
        size = size < 0 ? size + objects.length : size;
        if(size < index + 1) {
            return null;
        }
        return (E)objects[front];
    }

    public void reverse() {
        // current node
        LinkedNode<E> node = head.next;
        // previous node
        // point at start of reversed linked list
        LinkedNode<E> previous = null;

        while (node != null) {

            // store previous node
            // LinkedNode<E> temp = previous;
            // change node to next node
            // previous = node;
            // node = node.next;
            // change node direction
            // previous.next = temp;


            LinkedNode<E> next = node.next;
            node.next = previous;
            previous = node;
            node = next;

        }
        head.next = previous;
    }

    public void merge() {

    }

    private static class Test {

        public static void main(String[] args) {
            reverse(10);
        }

        static void testLast(int size) {
            Random random = new Random();
            SingleLinkedList<Integer> list = new SingleLinkedList<>();
            random.setSeed(0);
            for(int i = 0; i < size; i++) {
                list.add(i, random.nextInt());
            }

            random.setSeed(0);
            for(int i = 0; i < size; i++) {
                int value = list.last(i);
                int randomValue = random.nextInt();
                System.out.println("values: <" + value + ", " + randomValue + ">");
            }

        }

        static void reverse(int size) {
            Random random = new Random();
            SingleLinkedList<Integer> list = new SingleLinkedList<>();
            random.setSeed(0);
            for(int i = 0; i < size; i++) {
                list.add(i, random.nextInt());
            }
            list.reverse();
            random.setSeed(0);
            for(int i = 0; i < size; i++) {
                System.out.println("values: <" + list.get(i) + ", " + random.nextInt() + ">");
            }
        }
    }
}
