package playground.sort;

import java.util.Arrays;
import java.util.Comparator;

public class Merge implements Sort {

    /**
     * 将问题分解为若干个小的问题
     * @param array
     * @param comparator
     * @param <T>
     */
    public static <T> void sort(T[] array, Comparator<T> comparator) {
        sort(array, 0, array.length - 1, comparator);
    }

    public static <T> void sort(T[] array, int low, int high, Comparator<T> comparator) {
        if(high <= low) {
            return;
        }

        int mid = low + (high - low) / 2;
        sort(array, low, mid, comparator);
        sort(array, mid, high, comparator);
        merge(array, low, mid, high, comparator);
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
