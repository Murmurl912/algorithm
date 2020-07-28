package datastructure.non_linear.tree.binary;

import java.util.Iterator;

public interface BinaryTree<E> {


    public static enum DFSOrder{
        DFS_INORDER,
        DFS_PREORDER,
        DFS_POSTORDER,
    }

    public static enum BFSOrder {
        BFS_LEFT,
        BFS_RIGHT
    }

    public static class BinaryNode<E, T> {

        protected BinaryNode<E, T> parent;
        protected BinaryNode<E, T> left;
        protected BinaryNode<E, T> right;

        protected E data;
        protected T attribute;

        protected BinaryNode<E, T> parent() {
            return parent;
        }

        public BinaryNode<E, T> left() {
            return left;
        }

        public BinaryNode<E, T> right() {
            return right;
        }

        public T attribute() {
            return attribute;
        }

        public E data() {
            return data;
        }

        public void parent(BinaryNode<E, T> parent) {
            this.parent = parent;
        }

        public void left(BinaryNode<E, T> left) {
            this.left = left;
        }

        public void right(BinaryNode<E, T> right) {
            this.right = right;
        }

        public void attribute(T attribute) {
            this.attribute = attribute;
        }

        public void data(E data) {
            this.data = data;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public boolean hasRight() {
            return right != null;
        }
    }
}
