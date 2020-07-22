package algorithm.search;

import java.util.Arrays;
import java.util.Objects;

/**
 * Binary search can only be applied to
 * a data structure that can be random access
 * by index
 * linked list is generally not suitable for binary search
 * but skip list can
 * and data stored in the data structure can be sorted
 * data stored in data structure must be sorted
 * @param <Data>
 */
public interface BinarySearchable<Data extends Comparable<Data>> {

    private void indexCheck(int index) {
        if(isIndexOutOfBounds(index)) {
            throw new IndexOutOfBoundsException(index);
        }
    }
    public Data access(int index);

    public boolean isIndexOutOfBounds(int index);

    /**
     * search data in from low to high
     * if data is found return the index of data,
     * else return a negative value whose absolute value
     * point to inserting position
     * @param data data to be searched for index
     * @param low lower bounds of search range
     * @param high higher bounds of search range
     * @return if data is found in search, a positive index will be returned,
     *         else a negative point to inserting position will be returned
     */
    default public int binarySearch(Data data, int low, int high, boolean isAscend) {
        indexCheck(low);
        indexCheck(high);

        while (low <= high) {
            int middle = middle(data, low, high);
            Data item = access(middle);

            // item equals data
            if(equal(item, data)) {
                return middle;
            }

            // item < data
            if(less(item, data)) {
                // increase lower bounds
                if(isAscend) {
                    low = middle + 1;
                } else {
                    high = middle - 1;
                }
            } else { // item > data
                // decrease higher bounds
                if(isAscend) {
                    high = middle - 1;
                } else {
                    low = middle + 1;
                }
            }

        }

        return -low;
    }

    default int middle(Data data, int low, int high) {
        return (low + high) / 2;
    }

    default public boolean less(Data a, Data b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.compareTo(b) < 0;
    }

    default public boolean equal(Data a, Data b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.compareTo(b) == 0;
    }

    default public boolean large(Data a, Data b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.compareTo(b) > 0;
    }

    default public boolean largeOrEqual(Data a, Data b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.compareTo(b) >= 0;
    }

    default public boolean lessOrEqual(Data a, Data b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.compareTo(b) <= 0;
    }
}
