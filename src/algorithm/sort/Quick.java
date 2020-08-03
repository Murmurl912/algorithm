package algorithm.sort;

public class Quick {

    public static <T extends Comparable<T>> void sort(T[] array) {
        sort(array, 0, array.length - 1);
    }

    public static <T extends Comparable<T>> void sort(T[] array,
                                                      int low,
                                                      int high) {
        // when low >= high
        // there is no element left
        // to be sorted
        if(low >= high) {
            return;
        }

        int j = partition(array, low, high);
        // algorithm.sort lower part of array
        sort(array, low, j - 1);
        // algorithm.sort higher part of array
        sort(array, j + 1, high);
    }

    public static <T extends Comparable<T>> int partition(T[] array,
                                                          int low,
                                                          int high) {
        // partition array to array[low...i-1] array[i], array[i+1...high]
        int i = low;
        int j = high + 1;
        T item = array[low]; // partition item
        while (true) {
            // search element bigger than item
            while (array[++i].compareTo(item) < 0) {
                if(i == high) { // prevent index out of bounds
                    break;
                }
            }
            // i point element that bigger than item
            // or last element of array
            // 1 2 3 4 5

            // search element less than item
            while (item.compareTo(array[--j]) < 0) {
                if(j == low) { // prevent index out of bounds
                    // this is redundant
                    // array[low] could never less than itself
                    break;
                }
            }
            // j point element that less than item
            // or first element of array


            // if i = j
            // means that no element in array[low...i - 1]
            // is bigger than item
            // if i > j
            // means that no element in array[j + 1...high]
            // is less than item
            if(i >= j) {
                break; // exit main loop
            }

            // swap element
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        array[low] = array[j];
        array[j] = item;
        return j;
    }

    public static void main(String[] args) {
        Integer[] integers = new Integer[]{3, 2, 1, 0};
        sort(integers);
    }
}
