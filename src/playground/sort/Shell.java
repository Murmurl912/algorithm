package playground.sort;

import java.util.Arrays;
import java.util.Comparator;

public class Shell {

    public static <T> void sort(T[] array, Comparator<T> comparator) {
        int step = 1;

        while(step < array.length / 3) {
            step = 3 * step + 1;
        }

        System.out.println("Array: " + Arrays.toString(array));

        while (step >= 1) {

            System.out.println("Round: " + step);

            for(int i = step; i < array.length; i++) {

                // playground.sort array[step + k] array[2 * step + k] ....
                // where k = i % step

                T key = array[i];
                int j = i;
                for(j = i; j >= step; j -= step) {
                    // insert key into array[i - step] array[i - 2*step] ...
                    if(comparator.compare(key, array[j - step]) < 0) {
                        // key is less than array[j - step]

                        // move bigger element forward
                        array[j] = array[j - step];
                    } else {
                        // hit the break
                        break;
                    }
                }
                array[j] = key;
                System.out.println("index: " + i + " " + Arrays.toString(array));
            }

            step = step / 3;
        }
    }


    public static void main(String[] args) {
        Integer[] array = Sort.generate(15);
        sort(array, Integer::compareTo);
        System.out.println("array: " + Arrays.toString(array));
        assert  Sort.isOrdered(array, Integer::compareTo);
    }
}
