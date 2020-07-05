package datastructure.linear.sequential;

import datastructure.linear.BasicQueue;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * when queue is empty
 *   front
 * [        ,       ,       ]
 *    rear
 * when queue is not empty: size = (rear - 1 - front + 1) % length
 * size = (rear - front) & length
 * if every time size only increase by one
 * then size be be simplified by:
 * size = (rear - front < 0) ? (rear - front + length) : (rear - front)
 *    front
 * [   x    ,       ,       ]
 *             rear
 * when queue is full: size = length - 1
 * when is equivalent to below pseudocode
 * if rear - front > 0
 *      rear - front = length - 1
 *  else
 *      rear - front = - 1
 * is full: rear - front > 0 ? rear - front + 1 == length : rear - front + 1 == 0
 *
 *   front
 * [    x   ,   x   ,       ]
 *                     rear
 * @param <E>
 */
public class SequentialQueue<E> implements BasicQueue<E> {

    protected Object[] elements;
    protected int size = 0;
    public static final int MAX_QUEUE_SIZE = Integer.MAX_VALUE - 10;
    protected int front = 0;
    protected int rear = 0;

    public SequentialQueue() {
        elements = new Object[10];
    }

    @Override
    public boolean empty() {
        // front == rear
        return front == rear;
    }

    @Override
    public boolean full() {
        // (rear + 1) % length == front
        int i = rear + 1;
        if(i >= elements.length) {
            i = 0;
        }
        return i == front;
    }

    @Override
    public int size() {
        int size = rear - front;
        if(size <= 0) {
            size += elements.length;
        }
        return size;
    }

    @Override
    public void clear() {
        while (rear != front) {
            elements[front] = null;
            front = front + 1;
            if(front >= elements.length) {
                front = 0;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E take() {
        // queue is empty
        if(front == rear) {
            return null;
        }
        E element = (E)elements[front];
        elements[front] = null;

        // circularly increase front
        // this is equivalent to ++front % length
        int newFront = front + 1;
        if(newFront >= elements.length) {
            newFront = 0;
        }
        front = newFront;
        size--;
        return element;
    }

    @Override
    public void offer(E element) {

        int newRear = rear + 1;
        if(newRear >= elements.length) {
            newRear = 0;
        }

        // new rear meet front
        // break them
        if(newRear == front) {
            // queue is full
            int oldCapacity = elements.length;
            grow(elements.length + 1);
            int newCapacity = elements.length;
            // shift elements in left side of front
            // to the end of array
            // before shift:
            //                    |---- moved size ----|
            //              rear    front      old end                     new end
            // [4, 5, 6, 7, null  , 1     , 2, 3       , null, null, null, null   ]
            // |----------- old capacity --------------|---- increased space -----|
            // after shift:
            //                    |------ need to gc ------|
            //              rear    old front       old end       new front      new end
            // [4, 5, 6, 7, null  , 1        , 2, 3       , null, 1        , 2, 3       ]
            // |----------- old capacity -----------------|---- increased space --------|
            // gc:
            // [4, 5, 6, 7, null, null, null, null, 1, 2, 3]
            if (rear < front) {
                // move size: (oldCapacity - 1) - front + 1
                int moveSize = oldCapacity - front;
                // new capacity - 1 - new front + 1 = move size
                // new front = new capacity - move size
                int newFront = newCapacity - moveSize;
                System.arraycopy(
                        elements,
                        front,
                        elements,
                        newFront,
                        moveSize);
                for(int i = front; i < newFront; i++){
                    elements[i] = null;
                }
                front = newFront;
            }
        }

        elements[rear] = element;
        // this is wrong implementation
        // if newRear is 0
        // which actually should be rear + 1
        // rear = newRear;
        // below is a correct implementation
        newRear = rear + 1;
        if(newRear >= elements.length) {
            newRear = 0;
        }
        rear = newRear;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        if(front == rear) {
            return null;
        }
        return (E) elements[front];
    }

    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    protected void grow(int miniCapacity) {
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

    private class QueueIterator implements Iterator<E> {
        private int counter = 0;
        @Override
        public boolean hasNext() {
            return counter < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if(counter < size + 1 && counter > -1) {
                int index = front + counter;
                if(index >= elements.length) {
                    index = 0;
                }
                counter++;
                return (E)elements[index];
            }
            return null;
        }
    }

    private static class Test {
        public static void main(String[] args) {
            testTakeAndOfferAndPeek(1000);
            clearTest(1000);
        }

        static void testTakeAndOfferAndPeek(int size) {
            Random random = new Random(System.currentTimeMillis());
            SequentialQueue<Integer> queue = new SequentialQueue<>();

            int[] inSequence = new int[size];
            int[] outSequence = new int[inSequence.length];

            random.setSeed(0);
            for(int i = 0; i < inSequence.length; i++) {
                inSequence[i] = random.nextInt();
            }
            int inIndex = 0;
            int outIndex = 0;
            while (inIndex < inSequence.length || outIndex < inSequence.length) {
                boolean flag = random.nextBoolean();
                if(queue.empty()) {
                    flag = true;
                }
                if(inIndex < inSequence.length && flag) {
                    int value = inSequence[inIndex++];
                    queue.offer(value);
                    value = queue.peek();
                    if(value != inSequence[outIndex]) {
                        System.out.println("Peek test failed");
                        return;
                    }
                } else {
                    int value = queue.peek();
                    if(value != inSequence[outIndex]) {
                        System.out.println("Peek test failed");
                        return;
                    }

                    value =  queue.take();
                    outSequence[outIndex++] = value;
                }

            }

            if(Arrays.equals(inSequence, outSequence)) {
                System.out.println("Test success");
            } else {
                System.out.println("Offer and take test failed");
            }
        }

        static void clearTest(int maxSize) {
            Random random = new Random();
            SequentialQueue<Integer> queue = new SequentialQueue<>();
            int size = random.nextInt(maxSize);
            random.setSeed(0);
            for(int i = 0; i < size; i++) {
                queue.offer(random.nextInt());
            }

            random.setSeed(0);
            for(Integer integer : queue) {
                if(integer != random.nextInt()) {
                    System.out.println("Test failed");
                    return;
                }
            }
            queue.clear();

            int[] inSequence = new int[size];
            int[] outSequence = new int[inSequence.length];

            random.setSeed(0);
            for(int i = 0; i < inSequence.length; i++) {
                inSequence[i] = random.nextInt();
            }
            int inIndex = 0;
            int outIndex = 0;
            while (inIndex < inSequence.length || outIndex < inSequence.length) {
                boolean flag = random.nextBoolean();
                if(queue.empty()) {
                    flag = true;
                }
                if(inIndex < inSequence.length && flag) {
                    int value = inSequence[inIndex++];
                    queue.offer(value);
                    value = queue.peek();
                    if(value != inSequence[outIndex]) {
                        System.out.println("Peek test failed");
                        return;
                    }
                } else {
                    int value = queue.peek();
                    if(value != inSequence[outIndex]) {
                        System.out.println("Peek test failed");
                        return;
                    }

                    value =  queue.take();
                    outSequence[outIndex++] = value;
                }

            }

            if(Arrays.equals(inSequence, outSequence)) {
                System.out.println("Test success");
            } else {
                System.out.println("Offer and take test failed");
            }

        }
    }
}
