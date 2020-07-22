package algorithm.search;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Set;

/**
 * using hash table to search data index
 * @param <Data>
 */
public interface HashSearchable<Data> {

    public int size();

    public int element(int index);

    /**
     * resolve conflict at index
     * @param index index where conflict occurs
     * @param hashCode hashCode of data
     * @return next index
     */
    public int conflict(int index, int hashCode);

    default int hashSearch(Data data) {
        int hashCode = data.hashCode();
        int index = index(data.hashCode());
        if(Objects.equals(element(index), data)) {
            return index;
        }

        while (!Objects.equals(element((index = conflict(index, hashCode))), data)) {
            // do nothing
        }

        return index;
    }

    /**
     * Obtain index from hashcode
     * @param hashCode hashcode of element
     * @return index of element
     */
    default int index(int hashCode) {
        return hashCode % size();
    }
}
