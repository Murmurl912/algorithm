package playground;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TwoSum {

    public static Map<Integer, Integer> count(int sum, int[] array) {
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

    public static void main(String[] args) {
        int[] array = new Random().ints(20, 0, 20)
                .toArray();
        Map<Integer, Integer> map = count(21, array);
        System.out.println(Arrays.toString(array));
        map.forEach((k, v) -> {
            System.out.println("<" + array[k] + ", " + array[v] + ">");
        });
    }
}
