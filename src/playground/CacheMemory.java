package playground;

import java.util.HashMap;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Random;

/**
 * sequential read is two times faster than non non sequential read
 */
public class CacheMemory {

    static Random random = new Random();
    public static void main(String[] args) {
        int[][] arrays = new int[1000][1000];

        Map<Integer, Long> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            sequential(arrays);
            map.put(i, System.nanoTime() - start);
        }

        LongSummaryStatistics sequential = map.values().stream().mapToLong(a -> a)
                .summaryStatistics();

        map.clear();
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            nonsequentail(arrays);
            map.put(i, System.nanoTime() - start);
        }
        LongSummaryStatistics nonsequential = map.values().stream().mapToLong(a -> a)
                .summaryStatistics();

        System.out.println("Sequential: " + sequential);
        System.out.println("Non Sequential: " + nonsequential);
    }

    public static void sequential(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int k = 0; k < array[i].length; k++) {
                array[i][k] = random.nextInt();
            }
        }
    }

    public static void nonsequentail(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[j][i] = random.nextInt();
            }
        }
    }
}
