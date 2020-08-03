package algorithm.sort;

public class Quick3Way {
    private static <T extends Comparable<T>> void sort(T[] array, int low, int high) {
        if(low >= high) {
            return;
        }

        int less = low;
        int i = low + 1;
        int greater = high;
        T v = array[low];

        while (i <= greater) {
            int cmp = array[i].compareTo(v);
            if(cmp < 0) {
                T temp = array[less];
                array[less++] = array[i];
                array[i++] = temp;
            } else if(cmp > 0) {
                T temp = array[greater];
                array[greater--] = array[i];
                array[i] = temp;
            } else {
                i++;
            }
        }
        // array[low...less - 1] < v = array[less...greater] < array[greater + 1...high]
        sort(array, low, less - 1);
        sort(array, greater + 1, high);
    }
}
