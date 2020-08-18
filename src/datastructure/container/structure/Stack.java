package datastructure.container.structure;

import datastructure.container.Container;

public interface Stack<E> extends Container<E> {

    public E peek();

    public boolean push(E data);

    public E pop();
}
