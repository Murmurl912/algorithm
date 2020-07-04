package datastructure.linear;

public interface BasicQueue<E> extends Iterable<E> {

    public int size();

    public void clear();

    public E take();

    public void offer(E element);

    public E peek();

    public boolean empty();

    public boolean full();

}
