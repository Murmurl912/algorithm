package datastructure.non_linear.tree;

import datastructure.non_linear.tree.binary.BinaryTree;

public class BasicBinaryTree<E, T> implements BinaryTree<E, T> {

    protected BinaryNode<E, T> root;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public BinaryNode<E, T> root() {
        return root;
    }

    @Override
    public BinaryNode<E, T> insert(E data) {
        root = new BinaryNode<>(
                null,
                null,
                null,
                data,
                null);
        return root;
    }

    @Override
    public int height(BinaryNode<E, T> node) {
        return 0;
    }

    @Override
    public int depth(BinaryNode<E, T> node) {
        return 0;
    }
}
