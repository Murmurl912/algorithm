package datastructure.container.structure;

import datastructure.container.Container;

import java.util.Iterator;

public interface BinaryTree<E> extends Container<E> {

    public static final int POST_ORDER = 0;
    public static final int PRE_ORDER = 1;
    public static final int IN_ORDER = 2;
    public static final int LEVEL_ORDER = 3;

    public int height();

    public BinaryNode<E> predecessor(int order, BinaryNode<E> node);

    public BinaryNode<E> successor(int order, BinaryNode<E> node);

    public Iterator<BinaryNode<E>> iterator(int order);

    public static interface BinaryNode<E> {
        public BinaryNode<E> left();

        public BinaryNode<E> right();

        public BinaryNode<E> parent();

        public E data();

        public int height();

        public int depth();
    }
}
