package datastructure.linear;

public interface BasicArray<E> extends Iterable<E> {

    public int size();

    public void add(int index, E element);

    public E get(int index);

    public E remove(int index);

    public int remove(E element);

    public int first(int start, E element);

    public int last(int end, E element);

    public void clear();

    public void set(int index, E element);

}
