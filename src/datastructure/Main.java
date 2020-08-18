package datastructure;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        random.setSeed(0);
        ArrayList<Integer> integers = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            integers.add(random.nextInt(5));
        }
        integers.removeAll(List.of(1));
    }
}
