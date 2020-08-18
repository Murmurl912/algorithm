package datastructure.container.structure;

public interface BinaryHeap<E> extends BinaryTree<E> {

    public E remove();

    public void add(E data);

    public E peek();

    public void merge(BinaryHeap<E> that);

}
