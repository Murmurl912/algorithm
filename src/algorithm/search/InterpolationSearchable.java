package algorithm.search;

/**
 * interpolation search take advantage of evenly distributed data
 *
 * @param <Data>
 */
public interface InterpolationSearchable<Data extends Comparable<Data>> extends BinarySearchable<Data> {

    /**
     * probing position of comparing index
     * @param data data to search for
     * @param low lower bounds of search range
     * @param high high bounds of search range
     * @return probing index
     */
    @Override
    default int middle(Data data, int low, int high) {
        double ratio = distance(data, access(high));
        return (int)(ratio * (high - low)) + low;
    }

    /**
     * distance between a and b
     * generally is implemented like distance = abs(a - b)
     * should return a absolute value
     * @param a a data
     * @param b another data
     * @return distance between two data
     */
    public double distance(Data a, Data b);
}
