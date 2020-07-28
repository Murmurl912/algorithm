package datastructure.non_linear.tree.binary;

import java.util.*;
import java.util.function.Consumer;

public interface BinaryTree<E, T> {

    public int size();

    public BinaryNode<E, T> root();

    public BinaryNode<E, T> insert(E data);

    default public BinaryNode<E, T> insertLeft(BinaryNode<E, T> node, E data) {
        Objects.requireNonNull(node);
        if(node.hasLeft()) {
            iae();
        }
        node.left = new BinaryNode<>(node, null, null, data, null);
        return node.left;
    }

    default public BinaryNode<E, T> insertRight(BinaryNode<E, T> node, E data) {
        Objects.requireNonNull(node);
        if(node.hasRight()) {
            iae();
        }
        node.right = new BinaryNode<>(node, null, null, data, null);
        return node.right;
    }

    public int height(BinaryNode<E, T> node);

    public int depth(BinaryNode<E, T> node);

    default public Iterator<BinaryNode<E, T>> preorder() {
        return new PreOrderIterator<>(root());
    }

    default public Iterator<BinaryNode<E, T>> postorder() {
        return null;
    }

    default public Iterator<BinaryNode<E, T>> inorder() {
        return null;
    }

    default public Iterator<BinaryNode<E, T>> levelorder() {
        return null;
    }

    private void iae() {
        throw new IllegalArgumentException();
    }

    public class BinaryNode<E, T> {

        protected BinaryNode<E, T> parent;
        protected BinaryNode<E, T> left;
        protected BinaryNode<E, T> right;

        protected E data;
        protected T attribute;

        public BinaryNode(BinaryNode<E, T> parent,
                          BinaryNode<E, T> left,
                          BinaryNode<E, T> right,
                          E data,
                          T attribute) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.data = data;
            this.attribute = attribute;
        }

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

    public class PreOrderIterator<E, T> implements Iterator<BinaryNode<E, T>> {
        protected Deque<BinaryNode<E, T>> stack;
        protected BinaryNode<E, T> root;

        public PreOrderIterator(BinaryNode<E, T> root) {
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
        public BinaryNode<E, T> next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            // visit current node
            BinaryNode<E, T> current = stack.pop();

            // look up child

            // right child enters first
            // exits last
            if(current.hasRight()) {
                stack.push(current.left);
            }

            // left child enters last
            // exits first
            if(current.hasLeft()) {
                stack.push(current.left);
            }

            return current;
        }
    }

    public class PreOrderCommonIterator<E, T> implements Iterator<BinaryNode<E, T>> {
        protected Deque<BinaryNode<E, T>> stack;
        protected BinaryNode<E, T> root;
        protected BinaryTree.BinaryNode<E, T> current;

        protected BinaryNode<E, T> computeCurrent;
        protected Deque<BinaryNode<E, T>> computeStack;

        public PreOrderCommonIterator(BinaryNode<E, T> root) {
            this.root = root;
            this.stack = new ArrayDeque<>();
            this.computeStack = new ArrayDeque<>();
            current = root;
            if(root != null) {
                stack.push(root);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty() || current != null;
        }

        @Override
        public BinaryNode<E, T> next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            BinaryNode<E, T> node = null;
            if(current != null) {
                // visit along left
                node = current;
                if(current.hasRight()) {
                    // push root of right branch to stack
                    // fifo
                    stack.push(current.left);
                }
                current = current.left;
            } else {
                // left branch has been visited
                // pop out a new branch root
                current = stack.pop();
                node = current;
            }

            return node;
        }


        private void visitAlongLeftBranch(Consumer<BinaryNode<E, T>> consumer) {

            while (computeCurrent != null) {
                consumer.accept(computeCurrent);
                if(computeCurrent.hasRight()) {
                    // push left child to stack
                    computeStack.push(computeCurrent.right);
                }
                // point to next left child
                computeCurrent = computeCurrent.left;
            }
        }

        public void compute(Consumer<BinaryNode<E, T>> consumer) {
            computeCurrent = root;
            computeStack.clear();
            while (true) {
                visitAlongLeftBranch(consumer);
                if(computeStack.isEmpty()) {
                    break;
                }
                computeCurrent = computeStack.pop();
            }
        }
    }

    public class InOrderIterator<E, T> implements Iterator<BinaryNode<E, T>> {
        protected Deque<BinaryNode<E, T>> stack;
        protected BinaryNode<E, T> root;
        protected BinaryNode<E, T> current;

        protected Deque<BinaryNode<E, T>> computeStack;
        protected BinaryNode<E, T> computeRoot;

        public InOrderIterator(BinaryNode<E, T> root) {
            stack = new ArrayDeque<>();
            this.root = root;
            current = root;

            computeStack = new ArrayDeque<>();
            computeRoot = root;

        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty() || current != null;
        }

        @Override
        public BinaryNode<E, T> next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            // go along left branch
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // stack is not empty and current is null
            BinaryNode<E, T> node = stack.pop();
            current = node.right;

            return node;
        }

        private void goAlongLeftBranch() {
            while (computeRoot != null) {
                computeStack.push(computeRoot);
                computeRoot = computeRoot.left;
            }
        }

        public void compute(Consumer<BinaryNode<E, T>> consumer) {
            while (true) {
                goAlongLeftBranch();
                if(computeStack.isEmpty()) {
                    break;
                }
                computeRoot = computeStack.pop();
                consumer.accept(computeRoot);
                computeRoot = computeRoot.right;
            }
        }

    }
}
