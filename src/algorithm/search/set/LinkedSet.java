package algorithm.search.set;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;
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
        size--;
        return true;
    }

    @Override
    public Set<E> union(Set<E> that) {
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
            thatNode = thatNode.next;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        Iterator<E> iterator = iterator();
        builder.append(iterator.hasNext() ? iterator.next() : "");
        iterator.forEachRemaining(e -> {builder.append(",").append(e);});
        builder.append("}");
        return builder.toString();
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

    private static class Test {

        public static void main(String[] args) {
            randomPutRemoveTest((int)(Math.abs(Math.random()) * 1000) + 1);
            randomContainTest((int)(Math.abs(Math.random()) * 1000) + 1);
            clearTest((int)(Math.abs(Math.random()) * 1000) + 1);
            unionTest((int)(Math.abs(Math.random()) * 1000) + 1, (int)(Math.abs(Math.random()) * 1000) + 1);
            intersectTest((int)(Math.abs(Math.random()) * 1000) + 1, (int)(Math.abs(Math.random()) * 1000) + 1);
            complementTest((int)(Math.abs(Math.random()) * 1000) + 1, (int)(Math.abs(Math.random()) * 1000) + 1);
            subsetTest((int)(Math.abs(Math.random()) * 1000) + 1, (int)(Math.abs(Math.random()) * 1000) + 1);
            ;
        }

        static void randomPutRemoveTest(int iteration) {
            Set<Integer> set = new LinkedSet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put: ");
            while (count++ < iteration) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + set.put(value) + ", " + set);
            }
            System.out.println("Remove: ");
            random.setSeed(0);
            while (count-- > 0) {
                int value = random.nextInt(100);
                System.out.println("Remove: " + value + ", " + set.remove(value) + ", " + set);
            }
            boolean result = set.isEmpty();
            System.out.println("Put Remove Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void randomContainTest(int iteration) {
            Set<Integer> set = new LinkedSet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put: ");
            while (count++ < iteration) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + set.put(value) + ", " + set);
            }

            boolean result = true;
            System.out.println("Contain: ");
            random.setSeed(0);
            while (count-- > 0) {
                int value = random.nextInt(100);
                result = result && set.contains(value);
                System.out.println("Contain: " + value + ", " + set.contains(value) + ", " + set);
            }

            System.out.println("Contain Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void clearTest(int size) {
            Set<Integer> set = new LinkedSet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put: ");
            while (count++ < size) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + set.put(value) + ", " + set);
            }

            set.clear();
            boolean result = set.isEmpty();
            System.out.println("Clear Test Result: " + set.isEmpty());
            if(!result) {
                throw new RuntimeException("Test Failed");
            }
        }

        static void unionTest(int sizeA, int sizeB) {
            Set<Integer> setA = new LinkedSet<>();
            Set<Integer> setB = new LinkedSet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put Set A: ");
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setA.put(value) + ", " + setA);
            }
            count = 0;
            System.out.println("Put Set B: ");
            while (count++ < sizeB) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setB.put(value) + ", " + setB);
            }

            Set<Integer> setC = setA.union(setB);
            System.out.println("Union Result: " + setC);

            for(Integer integer : setA) {
                setC.remove(integer);
            }

            for(Integer integer : setB) {
                setC.remove(integer);
            }

            boolean result = setC.isEmpty();

            System.out.println("Union Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Union Test Failed");
            }
        }

        static void intersectTest(int sizeA, int sizeB) {
            Set<Integer> setA = new LinkedSet<>();
            Set<Integer> setB = new LinkedSet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put Set A: ");
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setA.put(value) + ", " + setA);
            }
            count = 0;
            System.out.println("Put Set B: ");
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                System.out.println("Add: " + value + ", " + setB.put(value) + ", " + setB);
            }

            Set<Integer> setC = setA.intersect(setB);

            boolean flag = true;
            for(Integer integer : setC) {

                if(!setA.contains(integer)
                        || !setB.contains(integer)) {
                    flag = false;

                    break;
                }

            }

            if(flag) {
                for(Integer integer : setA) {
                    if(!setC.contains(integer) && setB.contains(integer)) {
                        flag = false;
                        break;
                    }
                }
            }

            if(flag) {
                for(Integer integer : setB) {
                    if(!setC.contains(integer) && setA.contains(integer)) {
                        flag = false;
                        break;
                    }
                }
            }
            System.out.println("Intersect Result: " + setC);
            System.out.println("Intersect Test Result: " + flag);
            if(!flag) {
                throw new RuntimeException("Intersect Test Failed");
            }

        }

        static void complementTest(int sizeA, int sizeB) {
            Set<Integer> setA = new LinkedSet<>();
            Set<Integer> setB = new LinkedSet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put Set A: ");
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setA.put(value) + ", " + setA);
            }
            count = 0;
            System.out.println("Put Set B: ");
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                System.out.println("Add: " + value + ", " + setB.put(value) + ", " + setB);
            }

            Set<Integer> setC = setA.complement(setB);

            boolean flag = true;
            for(Integer integer : setC) {

                if(setA.contains(integer)
                        && setB.contains(integer)) {
                    flag = false;

                    break;
                }

            }

            if(flag) {
                for(Integer integer : setA) {
                    // integer in a, but not in b and not c
                    if(!setB.contains(integer) && !setC.contains(integer)) {
                        flag = false;
                        break;
                    }
                }
            }

            System.out.println("Complement Result: " + setC);
            System.out.println("Complement Test Result: " + flag);
            if(!flag) {
                throw new RuntimeException("Complement Test Failed");
            }
        }

        static void subsetTest(int sizeA, int sizeB) {
            Set<Integer> setA = new LinkedSet<>();
            Set<Integer> setB = new LinkedSet<>();
            Random random = new Random();
            random.setSeed(0);

            int count = 0;
            System.out.println("Put Set A: ");
            while (count++ < sizeA) {
                int value = random.nextInt(100);
                System.out.println("Add: " + value + ", " + setA.put(value) + ", " + setA);
            }
            count = 0;
            System.out.println("Put Set B: ");
            while (count++ < sizeB) {
                int value = random.nextInt(1000);
                System.out.println("Add: " + value + ", " + setB.put(value) + ", " + setB);
            }

            boolean result =  !setA.isSubsetOf(setB);
            System.out.println("Set A: " + setA);
            System.out.println("Set B: " + setB);
            System.out.println("Negative Subset Test: " + result);


            setB.clear();
            result = result && setB.isSubsetOf(setA);
            System.out.println("Empty Set Test: " + result);

            sizeB = (Math.abs((int)(Math.random() * sizeA) + 1) % (sizeA + 1));
            random.setSeed(0);
            count = 0;
            while (count++ < sizeB) {
                int value = random.nextInt(100);
                setB.put(value);
            }
            System.out.println("Set B: " + setB);
            result = result && setB.isSubsetOf(setA);
            System.out.println("Positive Subset Test: " + result);

            System.out.println("Is Subset Test Result: " + result);
            if(!result) {
                throw new RuntimeException("Intersect Test Failed");
            }

        }
    }

}
