package playground.sort;

import java.util.Arrays;
import java.util.Comparator;

public class MergeBU  implements Sort {

    public static <T> void sort(T[] array, Comparator<T> comparator) {
        for(int size = 1; size < array.length; size = size + size) {
            for(int low = 0; low < array.length - size; low += size + size) {
                merge(array,
                        low,
                        low + size - 1,
                        Math.min(low + size + size - 1, array.length - 1),
                        comparator);
            }
        }

    }

    public static <T> void merge(T[] array,
                                 int low,
                                 int middle,
                                 int high,
                                 Comparator<T> comparator) {
        // playground.sort array[low...mid] and array[middle+1...high]
        // then merge two array

        int i = low;
        int j = middle + 1;
        T[] aux = Arrays.copyOf(array, array.length);
        for(int k = low; k <= high; k++) {
            if(i > middle) {
                array[k] = aux[j++];
            } else if(j > high) {
                array[k] = aux[i++];
            } else if(comparator.compare(aux[j], aux[i]) < 0) {
                array[k] = aux[j++];
            } else {
                array[k] = aux[i++];
            }
        }
    }
}
