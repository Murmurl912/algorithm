package playground.sort;

import java.util.Comparator;
import java.util.Random;

public interface Sort {

    public static <T> void sort(T[] array, Comparator<T> comparator) {

    }

    public static <T> boolean less(T a, T b, Comparator<T> comparator) {
        return comparator.compare(a, b) < 0;
    }

    public static <T> void exchange(T[] array, int a, int b) {
        T temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    public static <T> boolean isOrdered(T[] array, Comparator<T> comparator) {
        if(array.length < 3) {
            return true;
        }

        // flag: true ascend
        // flag： false descend
        boolean flag = comparator.compare(array[0], array[array.length - 1]) <= 0;

        int index = 0;
        do {
            int result =
                    comparator.compare(array[index], array[index + 1]);
            if((result <= 0) != flag) {
                return false;
            }

        } while (++index < array.length - 1);
        return true;
    }

    public static Integer[] generate(int size) {
        Random random = new Random();
        Integer[] randoms = new Integer[size];
        for(int i = 0; i < size; i++) {
            randoms[i] = random.nextInt(100);
        }
        return randoms;
    }

}
