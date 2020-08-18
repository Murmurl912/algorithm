package datastructure.non_linear.tree;

import java.util.Iterator;

public interface BinaryTree<E> {
    public int size();

    public int height(BinaryNode<E> node);

    public int depth(BinaryNode<E> node);

    public BinaryNode<E> root();

    public BinaryNode<E> insert(BinaryNode<E> parent, E data, boolean isLeftChild);

    public BinaryNode<E> remove(BinaryNode<E> parent, boolean isLeftChild);

    public BinaryNode<E> search(E data);

    public BinaryNode<E> precursor(BinaryNode<E> node, TraversalOrder order);

    public BinaryNode<E> successor(BinaryNode<E> node, TraversalOrder order);

    public Iterator<E> traversal(TraversalOrder order);

    public static enum TraversalOrder {
        PREORDER,
        INORDER,
        POSTORDER,
        LEVEL_ORDER
    }

}
