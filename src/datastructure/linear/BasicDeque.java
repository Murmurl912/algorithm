package datastructure.linear;

public interface BasicDeque<E> extends BasicQueue<E> {

    public void offerFront(E element);

    public void offerRear(E element);

    public E takeFront();

    public E takeRear();

    public E peekFront();

    public E peekRear();

}
