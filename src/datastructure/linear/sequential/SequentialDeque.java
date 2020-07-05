package datastructure.linear.sequential;

import datastructure.linear.BasicDeque;

import java.util.Arrays;
import java.util.Random;

public class SequentialDeque<E> extends SequentialQueue<E> implements BasicDeque<E> {

    @Override
    public void offerFront(E element) {
        int newFront = front - 1;
        if(newFront < 0) {
            newFront = elements.length - 1;
        }

        if(rear == newFront) {
            grow(elements.length + 1);
        }

        newFront = front - 1;
        if(newFront < 0) {
            newFront = elements.length - 1;
        }
        elements[newFront] = element;
        front = newFront;
        size++;
    }

    @Override
    public void offerRear(E element) {
        offer(element);
    }

    @Override
    public E takeFront() {
        return take();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E takeRear() {
        if(rear == front) {
            return null;
        }
        int newRear = rear - 1;
        if(newRear < 0) {
            newRear = elements.length - 1;
        }
        E element = (E)elements[newRear];
        rear = newRear;
        size--;
        return element;
    }

    @Override
    public E peekFront() {
        if(rear == front) {
            return null;
        }
        return peek();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peekRear() {
        if(rear == front) {
            return null;
        }
        return (E)elements[rear - 1];
    }

    private static class Test {

        public static void main(String[] args) {
            testTakeAndOfferAndPeek(10000);
        }

        static void testTakeAndOfferAndPeek(int size) {
            Random random = new Random(System.currentTimeMillis());
            SequentialDeque<Integer> queue = new SequentialDeque<>();

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
                    queue.offerRear(value);
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

                    value =  queue.takeFront();
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
