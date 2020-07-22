package algorithm.search.set;

/**
 * set stores unique item
 * set can be implemented
 * with unordered array,
 *      ordered array
 *      unordered list
 *      binary search tree
 *      balanced binary search tree
 *      hash table
 *
 * @param <E> type of element to be stored
 */
public interface Set<E> extends Iterable<E> {

    public int size();

    public int capacity();

    public boolean isEmpty();

    public void clear();

    public boolean put(E element);

    public boolean contains(E element);

    public boolean remove(E element);

    /**
     * set = this + that
     * @param that set to union with
     * @return a new of union
     */
    public Set<E> union(Set<E> that);

    /**
     * set = this - (this - that)
     * @param that set to intersect
     * @return a new set of intersection
     */
    public Set<E> intersect(Set<E> that);

    /**
     * relative complement
     * set = this - that
     * @param that set to complement
     * @return a new set of compllement
     */
    public Set<E> complement(Set<E> that);

    /**
     * test if this set is subset of given set
     * @param that given set
     * @return true if this set is subset of that set
     */
    public boolean isSubsetOf(Set<E> that);

}
