package playground;

import util.recorder.StopWatch;

import java.util.HashMap;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Random;

public class MemoryCopy {

    public static Random random = new Random();

    public static void main(String[] args) {
        // 2^3 * 2^26 = 2^28 = 64 mbyte
        int[] array = random(0x04000000);
        int[] target = new int[array.length];

        // test run
        for (int i = 0; i < 4; i++) {
            memeorycopy(array, target);
        }

        Map<Integer, Long> memoryCopyRecords = new HashMap<>();
        long start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            start = System.nanoTime();
            memeorycopy(array, target);
            memoryCopyRecords.put(i, System.nanoTime() - start);
        }

        for (int i = 0; i < 4; i++) {
            memeoryswap(array, target);
        }

        Map<Integer, Long> memorySwapRecords = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            start = System.nanoTime();
            memeoryswap(array, target);
            memorySwapRecords.put(i, System.nanoTime() - start);
        }

        LongSummaryStatistics copy = memoryCopyRecords.values().stream().mapToLong(a -> a)
                .summaryStatistics();

        LongSummaryStatistics swap = memorySwapRecords.values().stream().mapToLong(a -> a)
                .summaryStatistics();

        System.out.println("Memory Copy: " + copy);
        System.out.println("Memory Swap: " + swap);
    }

    public static void memeorycopy(int[] array, int[] target) {
        System.arraycopy(array, 0, target, 0, array.length);
    }

    public static void memeoryswap(int[] array, int[] target) {
        for (int i = 0; i < array.length; i++) {
            target[i] = array[i];
        }
    }

    public static int[] random(int size) {
        return random.ints(size).toArray();
    }
}
