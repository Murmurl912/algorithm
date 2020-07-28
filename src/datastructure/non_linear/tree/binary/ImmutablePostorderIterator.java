package datastructure.non_linear.tree.binary;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;

public class ImmutablePostorderIterator<E, T> implements Iterator<BinaryTree.BinaryNode<E, T>> {
    protected BinaryTree.BinaryNode<E, T> root;
    protected Deque<BinaryTree.BinaryNode<E, T>> stack;
    protected BinaryTree.BinaryNode<E, T> current;
    protected Deque<BinaryTree.BinaryNode<E, T>> parents;
    protected Deque<BinaryTree.BinaryNode<E, T>> postorder;

    public ImmutablePostorderIterator(BinaryTree.BinaryNode<E, T> root) {
        stack = new ArrayDeque<>();
        this.root = root;
        stack.push(root);
        current = root;

        parents = new ArrayDeque<>();
        postorder = new ArrayDeque<>();

        if(root != null) {
            parents.push(root);
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public BinaryTree.BinaryNode<E, T> next() {

        return null;
    }

    public void compute(Consumer<BinaryTree.BinaryNode<E, T>> consumer) {
        while (!parents.isEmpty()) {
            BinaryTree.BinaryNode<E, T> top = parents.pop();
            postorder.push(top);
            if(top.hasLeft()) {
                parents.push(top.left());
            }
            if(top.hasRight()) {
                parents.push(top.right());
            }
        }
        while (!postorder.isEmpty()) {
            consumer.accept(postorder.pop());
        }
    }

}
