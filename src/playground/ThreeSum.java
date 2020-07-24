package playground;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ThreeSum {

    public static void three(int sum, int[] array) {

        // sort
        Arrays.sort(array);

        /*
        a + b + c = sum
        a <= b
        b <= c
        a <= sum - a - c

        c <= sum - 2a
        c >= b
         */
        for (int i = 0; i < array.length; i++) {

            int index = 0;
            int lastIndex = array.length;
            for (int j = i + 1; j < array.length; j++) {
                int key = sum - array[i] - array[j];
                if(key > sum - 2 * array[i] || key < array[j]) {
                    continue;
                }
                index = Arrays.binarySearch(array,
                        0,
                        Math.min(lastIndex, j),
                        key);
                if(index > -1) {
                    System.out.println("<" + array[i] + ", " +
                            array[j] + ", " + array[index] + ">");
                }
            }
        }
    }

    public static Map<Integer, Integer> two(int sum, int[] array) {
        Map<Integer, Integer> map = new HashMap<>();

        // sort
        Arrays.sort(array);

        int index = 0;
        int lastIndex = array.length;
        for (int i = 1; i < array.length; i++) {

            // as array[i] >= array[k], k < i
            // if array[i] + array[k] = sum
            //   then array[i] >= sum - > array[i]
            if(array[i] * 2 <= sum ) {
                continue;
            }

            // search from 0->min(i, lastIndex)
            // assume element in array is not repeated
            index = Arrays.binarySearch(
                    array,
                    0,
                    Math.min(lastIndex, i),
                    sum - array[i]);

            if(index > 0) {
                map.put(i, index);
                lastIndex = index;
            }
        }

        return map;
    }


}
