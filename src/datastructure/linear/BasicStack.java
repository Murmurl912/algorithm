package datastructure.linear;

public interface BasicStack<E> extends Iterable<E> {

    public int size();

    public void clear();

    public void push(E element);

    public E pop();

    public E peek();
}
