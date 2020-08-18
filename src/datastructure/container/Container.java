package datastructure.container;

public interface Container<E> extends Iterable<E> {

    public int size();

    public void clear();

    default boolean isEmpty() {
        return size() > 0;
    }
}
