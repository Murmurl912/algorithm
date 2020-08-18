package datastructure.container;

/**
 * an interface used to indicate a class
 * whose stored data can be random access by index
 * @param <E> type of stored data
 */
public interface RandomAccessible<E> {

    /**
     * get data at index
     * @param index index of data
     * @return data which index point to
     * @throws IndexOutOfBoundsException index out of bound
     */
    public E at(int index);

}
