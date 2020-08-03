package algorithm.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * N * N / 2 times compares
 * and
 * M times exchange
 */
public class Selection implements Sort {

    public static <T> void sort(T[] array, Comparator<T> comparator) {
        int length = array.length;

        for(int i = 0; i < length; i++) {
            int min = i;

            for(int j = i + 1; j < length; j++) {
                // search smallest item in rest of the array
                // array[i:length]

                if(Sort.less(array[j], array[min], comparator)) {
                    min = j;
                }
            }
            Sort.exchange(array, min, i);
        }
    }


    public static void main(String[] args) {
        Integer[] array = Sort.generate(10);
        sort(array, Integer::compareTo);
        System.out.println("array: " + Arrays.toString(array));
        assert  Sort.isOrdered(array, Integer::compareTo);
    }
}
