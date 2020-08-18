package datastructure.container;

/**
 * an interface indicate that a class
 * can grow its internal storage capacity
 */
public interface Growable {

    /**
     * grow class internal storage size
     * by a minimal of min
     * @param min minimal of required size
     * @throws OutOfMemoryError out of memory
     */
    public void grow(int min);

    /**
     * trim class's internal storage
     * size down to a minimal size of min
     * @param min minimal size
     * @throws IllegalArgumentException
     *  min less than size of stored item
     *  in the object
     */
    public void trim(int min);
}
