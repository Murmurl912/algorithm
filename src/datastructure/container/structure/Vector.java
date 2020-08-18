package datastructure.container.structure;

import datastructure.container.Container;
import datastructure.container.Sortable;

public interface Vector<E> extends Container<E>, Sortable<E> {

    public E get(int index);

    public void add(E data, int index);

    public void replace(E data, int index);

    public int replace(E target, E data, int from, int to);

    public E remove(int index);

    public int remove(E data, int from, int to);

    public int search(E data, int from, int to);

    public void reverse();

    public Vector<E> slice(int from, int step, int to);
}
