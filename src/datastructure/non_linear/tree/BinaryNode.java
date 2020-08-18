package datastructure.non_linear.tree;

public class BinaryNode<E> {

    protected E data;
    protected BinaryNode<E> left;
    protected BinaryNode<E> right;
    protected BinaryNode<E> parent;

    protected BinaryNode(E data,
                      BinaryNode<E> left,
                      BinaryNode<E> right,
                      BinaryNode<E> parent) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }

    E data() {
        return data;
    }

    protected void data(E data) {
        this.data = data;
    }

    protected BinaryNode<E> left() {
        return left;
    }

    protected void left(BinaryNode<E> left) {
        this.left = left;
    }

    protected BinaryNode<E> right() {
        return right;
    }

    protected void right(BinaryNode<E> right) {
        this.right = right;
    }

    protected BinaryNode<E> parent() {
        return parent;
    }

    protected void parent(BinaryNode<E> parent) {
        this.parent = parent;
    }

}
