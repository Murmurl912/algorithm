package datastructure.container.structure;

import datastructure.container.Container;

public interface Deque<E> extends Container<E> {

    public void offerFront(E data);

    public void offerRear(E data);

    public E takeFront();

    public E takeRear();

    public E peekFront();

    public E peekRear();
}
