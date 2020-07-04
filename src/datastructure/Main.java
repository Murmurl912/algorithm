package datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
