package playground;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        int size = 0x08000000; // 2 ^ 27
        Random random = new Random();
        // object header 12 byte
        // int 4 byte
        // reference 4 byte (or 8 byte)

        // if an integer take 24 byte
        // 2 ^ 26 integers need 1 gb for integer object 0.5 gb for reference
        Runtime runtime = Runtime.getRuntime();
        System.out.println("Start: " + usedMemory(runtime) + " m bytes");
        ArrayList<Integer> array = new ArrayList<>(size);
        // should use 4 * 2 ^ 27 byte = 2 ^ 29 byte = 2 ^ -1 gb
        System.out.println("Create Array List: " + usedMemory(runtime) + " m bytes");

        for(int i = 0; i < size; i++) {
            array.add(random.nextInt());
        }
        System.out.println("Add Integers: " + usedMemory(runtime) + " m bytes");
    }

    static int usedMemory(Runtime runtime) {
        return (int)((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
    }
}
