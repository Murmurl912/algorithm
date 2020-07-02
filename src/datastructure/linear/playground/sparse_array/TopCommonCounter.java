package datastructure.linear.playground.sparse_array;

import java.util.*;

public class TopCommonCounter {

    /**
     * first use hash map store frequency
     * then using quick sort sort key value pair in hashmap by value
     * then slice top n from stored array
     * @param array input array to count frequency
     * @param topN number of top
     * @param <T> type
     * @return sorted frequency
     */
    public static <T> List<Map.Entry<T, Integer>>
        countTopNRepeatedHashMap(ArrayList<T> array, int topN) {

        HashMap<T, Integer> counter = new HashMap<>();
        // count frequency for each key
        // linear time complexity o(n)
        array.forEach(item -> {
            int frequency = counter.getOrDefault(item, 1);
            counter.put(item, frequency);
        });
        // obtain key value pair
        Set<Map.Entry<T, Integer>> pairs = counter.entrySet();

        // time complexity o(n log(n))
        // quick sort
        ArrayList<Map.Entry<T, Integer>> pairArray = new ArrayList<>(pairs);

        // slice array to topn
        pairArray.sort((a, b) -> a.getValue() - b.getValue());
        return pairArray.subList(0, Math.min(topN, array.size()));
    }

    public static <T> PriorityQueue<Map.Entry<T, Integer>>
        countTopNRepeatedHashMapPriorityQueue(ArrayList<T> array, int topN) {
        HashMap<T, Integer> counter = new HashMap<>();
        // count frequency for each key
        // linear time complexity o(n)
        array.forEach(item -> {
            int frequency = counter.getOrDefault(item, 1);
            counter.put(item, frequency);
        });
        PriorityQueue<Map.Entry<T, Integer>> priorityQueue = new PriorityQueue<>(topN, (a, b) -> a.getValue() - b.getValue());
        counter.forEach((item, frequency) -> {
            priorityQueue.offer(Map.entry(item, frequency));
        });
        return priorityQueue;
    }

    /**
     * this is a naive approach to find top n most frequent element in an array
     * with o(topn) space complexity
     * and o(n**2 + n(log(topn)) time complexity
     * @param array array of element
     * @param topN top n
     * @param <T> type of element
     * @return a key value pair key is top n element value is the frequent
     */
    public static <T> KeyValuePair<Object[], int[]> countTopNRepeatedNaive(ArrayList<T> array, int topN) {
        Object[] tops = new Object[topN];
        int[] counts = new int[topN];
        // loop n times
        // time complexity: n * (2 * log(topn)) + n(n-1)/2 = 2 * n * log(topn) + n**2
        outer_loop: for(int i = 0; i < array.size(); i++) {
            int count = 1;
            // count array[i] in rest of array
            // run time = sum(n-1, 1)
            for(int j = i + 1; j < array.size(); j++) {
                if(array.get(i) == array.get(j)) {
                    count++;
                }
            }

            // remove repeated
            // time complexity (log(topn))
            for(Object top : tops) {
                if(Objects.equals(top, array.get(i))) {
                    continue outer_loop;
                }
            }


            // binary search for insertion
            // time complexity log(topn)
            int low = 0;
            int high = topN - 1;
            int middle = 0;
            while (low <= high) {
                middle = low + (high - low) / 2;
                int value = counts[middle];
                if(value > count) {
                    low = middle + 1;
                } else if(value < count) {
                    high = middle -1;
                } else {
                    break;
                }
            }

            if(low < topN) {
                // insert
                // time complexity o(log(n))
                for(int k = topN - 1; k > low; k--) {
                    tops[k] = tops[k - 1];
                    counts[k] = counts[k - 1];
                }
                tops[low] = array.get(i);
                counts[low] = count;
            }
        }

        return new KeyValuePair<>() {

            private final Object[] key = tops;
            private final int[] value = counts;

            @Override
            public void setKey(Object[] objects) {

            }

            @Override
            public void setValue(int[] ints) {

            }

            @Override
            public Object[] getKey() {
                return key;
            }

            @Override
            public int[] getValue() {
                return value;
            }
        };
    }

    public static <T extends Comparable<T>> int search(T[] array, boolean asc, T element) {
        int lowIndex = 0;
        int highIndex = array.length - 1;
        int middleIndex = -1;

        while (lowIndex <= highIndex) {
            middleIndex = (highIndex - lowIndex) / 2 + lowIndex;
            System.out.println();
            System.out.print("low: <" + lowIndex + ", " + array[lowIndex] + "> ");
            System.out.print("middle: <" + middleIndex + ", " + array[middleIndex] + "> ");
            System.out.print("high: <" + highIndex + ", " + array[highIndex] + "> ");
            System.out.println();

            T middleValue = array[middleIndex];
            int compareResult = middleValue.compareTo(element);
            if(compareResult < 0) {
                System.out.println(middleValue + " < " + element);
                // middle value < element
                if(asc) {
                    // asc order
                    // element in right side of array
                    // increase left border
                    lowIndex = middleIndex + 1;
                } else {
                    // desc order
                    // element in left side of array
                    // decrease right border
                    highIndex = middleIndex - 1;
                }
            } else if(compareResult > 0){
                System.out.println(middleValue + " > " + element);
                // middle value > element
                if(asc) {
                    // asc order
                    // element in left side of array
                    // decrease right border
                    highIndex = middleIndex - 1;
                } else {
                    // desc order
                    // element in right side of array
                    // increase left border
                    lowIndex = middleIndex + 1;
                }
            } else {
                System.out.println(middleValue + " = " + element);
                return middleIndex;
            }
            System.out.println("low: " + lowIndex + ", high: " + highIndex);
            System.out.println();

        }

        return lowIndex;
    }

    public static void main(String[] args) {
        KeyValuePair<Object[], int[]> count = TopCommonCounter
                .countTopNRepeatedNaive(new ArrayList<>(List.of(5, 4, 3, 2, 1, 4, 3, 2, 1, 3, 2, 1, 2, 1, 1)), 5);
        System.out.println(Arrays.toString(count.getKey()));
        System.out.println(Arrays.toString(count.getValue()));
    }

}
