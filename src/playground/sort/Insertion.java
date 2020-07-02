package playground.sort;

import java.util.Arrays;
import java.util.Comparator;

public class Insertion implements Sort{

    public static <T> void sort(T[] array, Comparator<T> comparator) {

        for(int i = 1; i < array.length; i++) {
            T key = array[i];
            int j = i;
            for(j = i; j > 0; j--) { // when j = 0 loop end
                if(comparator.compare(key, array[j - 1]) < 0) {
                    // move bigger element forward
                    array[j] = array[j - 1];
                } else { // end loop if find insert position
                    break;
                }
            }
            array[j] = key;
        }
    }


    public static void main(String[] args) {
        Integer[] array = Sort.generate(10);
        sort(array, Integer::compareTo);
        System.out.println("array: " + Arrays.toString(array));
        assert  Sort.isOrdered(array, Integer::compareTo);
    }
}
