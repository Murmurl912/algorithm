package datastructure.container.structure;

import datastructure.container.Container;

public interface Queue<E> extends Container<E> {

    public int size();

    public boolean offer();

    public E take();

    public E peek();

    public void clear();
}
