package algorithm.search.set;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;

public class LinkedSet<E> implements Set<E> {
    protected int size;
    protected Node<E> head;
    protected final BiFunction<E, E, Boolean> DEFAULT_EQUAL;

    public LinkedSet() {
        DEFAULT_EQUAL = Objects::equals;
    }

    public LinkedSet(BiFunction<E, E, Boolean> equal) {
        DEFAULT_EQUAL = Objects.requireNonNullElseGet(
                equal,
                () -> Objects::equals);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int capacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        while (head != null) {
            Node<E> node = head;
            head = node.next;
            // gc node
            node.next = null;
            node.element = null;
            // decrease size
            size--;
        }
    }

    @Override
    public boolean put(E element) {
        Node<E>[] nodes = search(element);
        if(nodes[1] == null) {
            // not found
            head = new Node<>(element, head);
            size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(E element) {
        Node<E>[] nodes = search(element);
        return nodes[1] != null;
    }

    @Override
    public boolean remove(E element) {
        Node<E>[] nodes = search(element);
        if(nodes[1] == null) {
            // not found
            return false;
        }

        if(nodes[0] == null) {
            // remove head node;
            head = nodes[1].next;

            // gc
            nodes[1].next = null;
            nodes[1].element = null;
        } else {
            nodes[0].next = nodes[1].next;
            nodes[1].next = null;
            nodes[1].element = null;
        }
        return true;
    }

    @Override
    public Set<E> union(Set<E> that) {
        Iterator<E> self = this.iterator();
        Iterator<E> other = that.iterator();
        Set<E> set = this.copy();
        while (other.hasNext()) {
            // search time can be reduced using
            // hash or sort
            set.put(other.next());
        }
        return set;
    }

    @Override
    public Set<E> intersect(Set<E> that) {
        Iterator<E> self = this.iterator();
        Iterator<E> other = that.iterator();
        LinkedSet<E> set = new LinkedSet<>();
        while (self.hasNext()) {
            E element = self.next();
            if(that.contains(element)) {
                Node<E> temp = set.head;
                set.head = new Node<>(element, temp);
            }
        }
        return set;
    }

    @Override
    public Set<E> complement(Set<E> that) {
        Iterator<E> self = this.iterator();
        LinkedSet<E> set = new LinkedSet<>();
        while (self.hasNext()) {
            E element = self.next();
            if(!that.contains(element)) {
                Node<E> temp = set.head;
                set.head = new Node<>(element, temp);
            }
        }
        return set;
    }

    @Override
    public boolean isSubsetOf(Set<E> that) {
        for (E e : this) {
            if (!that.contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedIterator();
    }

    public LinkedSet<E> copy() {
        LinkedSet<E> set = new LinkedSet<>();
        if(head == null) {
            return set;
        }

        Node<E> thatNode = new Node<>(head.element, null);
        Node<E> thisNode = head.next;
        set.head = thatNode;
        set.size = 1;
        while (thisNode != null) {
            thatNode.next = new Node<>(thisNode.element, null);
            thisNode = thisNode.next;
            set.size++;
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    private Node<E>[] search(E element) {
        Node<E> previous = null;
        Node<E> current = head;
        while (current != null &&
                !DEFAULT_EQUAL.apply(element, current.element)) {
            previous = current;
            current = current.next;
        }
        Node<?>[] nodes = new Node<?>[2];
        nodes[0] = previous;
        nodes[1] = current;
        return (Node<E>[]) nodes;
    }

    private void remove(Node<E> previous, Node<E> target) {
        Objects.requireNonNull(target);
        if(previous == null) {
            // remove head
            head = target.next;
        } else {
            previous.next = target.next;
        }
        // decrease size counter
        size--;

        // gc
        target.element = null;
        target.next = null;
    }

    private class LinkedIterator implements Iterator<E> {
        Node<E> previous = null; // node before last
        Node<E> last = null; // node before cursor
        Node<E> cursor = head; // next node

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = cursor.element;
            previous = last;
            last = cursor;
            cursor = cursor.next;
            return element;
        }

        @Override
        public void remove() {
            if(last == null) {
                throw new NoSuchElementException();
            }

            LinkedSet.this.remove(previous, last);
        }
    }

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
    }
}
