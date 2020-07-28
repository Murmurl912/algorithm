package datastructure.non_linear.tree.binary;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ImmutablePreorderIterator<E> implements Iterator<BinaryTreeNode<E>> {
    protected Deque<BinaryTreeNode<E>> stack;
    protected BinaryTreeNode<E> root;

    public ImmutablePreorderIterator(BinaryTreeNode<E> root) {
        this.root = root;
        stack = new ArrayDeque<>();
        if(root != null) {
            stack.push(root);
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public BinaryTreeNode<E> next() {
        if(stack.isEmpty()) {
            throw new NoSuchElementException();
        }

        BinaryTreeNode<E> node = stack.peek();

        // push next node to be traveled
        if(node.hasLeft()) {
            if(!node.hasRight()) {
                // this node don't need to be traveled again
                stack.pop();
            }
            // push next left node
            stack.push(node.left());
            return node;
        }

        // pop current node
        node = stack.pop();
        // if node has right child
        if(node.hasRight()) {
            stack.push(node.right());
        }
        stack.push(node.right());
        return node;
    }
}
