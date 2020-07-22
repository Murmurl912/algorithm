package algorithm.search;

import java.util.Iterator;
import java.util.Objects;

/**
 * sequential search can be apply to an iterable data structure
 * in wich case, a Iterator provided by java util is used to
 * represent search result
 * @param <Data> type of data stored in data structure
 */
public interface SequentialSearchable<Data> extends Iterable<Data> {

    /**
     * return iterator whose next data point search result
     * if returned iterator does't have next, means search failed.
     * @param data data to be searched
     * @return an iterator whose next is point to data
     */
    default Iterator<Data> sequentialSearch(Data data) {
        Iterator<Data> iterator = iterator();
        Iterator<Data> resultIterator = iterator();
        while (iterator.hasNext()) {
            Data item = iterator.next();
            if(Objects.equals(data, item)) {
                break;
            }
            resultIterator.next();
        }
        return resultIterator;
    }

}
