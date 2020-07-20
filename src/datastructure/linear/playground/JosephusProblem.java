package datastructure.linear.playground;

import java.util.ArrayDeque;

public class JosephusProblem {

    public static void main(String[] args) {

    }

    public static ArrayDeque<Integer> josephus(int size,
                                               int number,
                                               int remaining) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        ArrayDeque<Integer> outs = new ArrayDeque<>();

        int counter = size;
        while (counter > remaining) {
            int out = counter - size;
            while (out < 0) {
                out += counter;
            }
            queue.add(out);
            counter--;
        }

        return queue;
    }


}
