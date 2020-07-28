package datastructure.non_linear.tree.binary;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ImmutableInorderIterator<E, T> implements Iterator<BinaryTree.BinaryNode<E, T>> {
    private final Deque<BinaryTree.BinaryNode<E, T>> stack;
    private BinaryTree.BinaryNode<E, T> next;

    public ImmutableInorderIterator(BinaryTree.BinaryNode<E, T> root) {
        stack = new ArrayDeque<>();
        next = root;
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty() && next == null;
    }

    @Override
    public BinaryTree.BinaryNode<E, T> next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }

        // travel far left leaf
        while (next != null) {
            stack.push(next);
            next = next.left();
        }

        BinaryTree.BinaryNode<E, T> node = stack.pop();
        next = node.right();
        return node;
    }
}
