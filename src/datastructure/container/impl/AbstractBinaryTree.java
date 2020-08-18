package datastructure.container.impl;

import datastructure.container.structure.BinaryTree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public abstract class AbstractBinaryTree<E> implements BinaryTree<E> {

    protected Node<E> root;
    protected int size;


    @Override
    public int height() {
        return root.height();
    }

    @Override
    public BinaryNode<E> predecessor(int order, BinaryNode<E> node) {
        switch (order) {
            case PRE_ORDER:
                return preorderPredecessor(node);
            case IN_ORDER:
                return inorderPredecessor(node);
            case POST_ORDER:
                return postorderPredecessor(node);
            case LEVEL_ORDER:
                return levelorderPredecessor(node);
            default:
                throw new IllegalArgumentException("invalid order");
        }
    }

    @Override
    public BinaryNode<E> successor(int order, BinaryNode<E> node) {
        switch (order) {
            case PRE_ORDER:
                return preorderSuccessor(node);
            case IN_ORDER:
                return inorderSuccessor(node);
            case POST_ORDER:
                return postorderSuccessor(node);
            case LEVEL_ORDER:
                return levelorderSuccessor(node);
            default:
                throw new IllegalArgumentException("invalid order");
        }
    }

    @Override
    public Iterator<BinaryNode<E>> iterator(int order) {
        switch (order) {
            case POST_ORDER:
                return new PreorderIterator<>(root);
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        //
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    protected BinaryNode<E> inorderPredecessor(BinaryNode<E> node) {
        return null;
    }

    protected BinaryNode<E> inorderSuccessor(BinaryNode<E> node) {
        return null;
    }

    protected BinaryNode<E> preorderPredecessor(BinaryNode<E> node) {
        return null;
    }

    protected BinaryNode<E> preorderSuccessor(BinaryNode<E> node) {
        return null;
    }

    protected BinaryNode<E> postorderPredecessor(BinaryNode<E> node) {
        return null;
    }

    protected BinaryNode<E> postorderSuccessor(BinaryNode<E> node) {
        return null;
    }

    protected BinaryNode<E> levelorderPredecessor(BinaryNode<E> node) {
        return null;
    }

    protected BinaryNode<E> levelorderSuccessor(BinaryNode<E> node) {
        return null;
    }

    protected static class Node<E> implements BinaryNode<E> {

        protected Node<E> parent;
        protected Node<E> left;
        protected Node<E> right;
        protected E data;

        @Override
        public BinaryNode<E> left() {
            return left;
        }

        @Override
        public BinaryNode<E> right() {
            return right;
        }

        @Override
        public BinaryNode<E> parent() {
            return parent;
        }

        @Override
        public E data() {
            return data;
        }

        @Override
        public int height() {
            return 1 +
                    Math.max(left == null ? -1 : left.height(),
                            right == null ? -1 : right.height());
        }

        @Override
        public int depth() {
            int count = 0;
            Node<E> node = parent;
            while (node != null) {
                node = node.parent;
                count++;
            }
            return count;
        }
    }

    private static class PreorderIterator<E> implements Iterator<BinaryNode<E>> {

        private Node<E> current;
        private final Deque<Node<E>> stack;

        public PreorderIterator(Node<E> root) {
            current = null;
            stack = new ArrayDeque<>();
            if(root != null) {
                stack.push(root);
            }
        }

        @Override
        public boolean hasNext() {
            return current != null || !stack.isEmpty();
        }

        @Override
        public BinaryNode<E> next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<E> node = null;

            if(current != null) {
                if(current.right != null) {
                    stack.push(current.right);
                }
                node = current; // visit left node
                current = current.left;
            } else {
                node = current = stack.pop(); // visit middle node
            }

            return node;
        }
    }

    private static class InorderIterator<E> implements Iterator<BinaryNode<E>> {
        // subtree to be visited
        private Node<E> current;
        private final Deque<Node<E>> stack;

        public InorderIterator(Node<E> root) {
            current = root;
            stack = new ArrayDeque<>();
            if(root != null) {
                stack.push(root);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty() || current != null;
        }

        @Override
        public BinaryNode<E> next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            // travel travel to left leaf of current subtree
            if(current != null) {
                stack.push(current);
                current = current.left;
            }


            // stack is not empty and current is null
            Node<E> node = stack.pop();
            // change subtree to right child
            current = node.right;

            return node;
        }
    }

    private static class PostorderIterator<E> implements Iterator<BinaryNode<E>> {

        private Node<E> current;
        private final Deque<Node<E>> stack;

        public PostorderIterator(Node<E> root) {
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
        public BinaryNode<E> next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }

            // travel to left leaf
            while (current != null) {
                if(current.right != null) {
                    stack.push(current.right);
                }
                stack.push(current);
                current = current.left;
            }


            return null;
        }
    }

    public static class LevelOrderIterator<E> implements Iterator<BinaryNode<E>> {
        protected Node<E> root;
        protected Deque<Node<E>> queue;

        public LevelOrderIterator(Node<E> root) {
            queue = new ArrayDeque<>();
            this.root = root;
            queue.push(root);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Node<E> next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<E> node = queue.remove();
            if(node.left != null) {
                queue.add(node.left);
            }
            if(node.right != null) {
                queue.add(node.right);
            }
            return node;
        }
    }

    private class Postorder {
        private final Node<E> root;
        private final Deque<Node<E>> stack;
        private Node<E> current;
        private final Consumer<BinaryNode<E>> consumer;

        public Postorder(Node<E> root, Consumer<BinaryNode<E>> consumer) {
            this.root = root;
            stack = new ArrayDeque<>();
            this.consumer = consumer;
        }

        private void init() {
            stack.clear();
            if(root != null) {
                stack.push(root);
            }
            current = null;
        }

        public void forEach() {
            init();
            while (!stack.isEmpty()) {
                if(current != null) {
                    if(current.right != null) {
                        stack.push(current.right);
                    }
                    stack.push(current);
                    current = current.left;
                    continue;
                }

                // when execute reach here
                // left branch has been stored


                current = stack.pop();
                if(current.right != null && current.right == stack.peek()) {

                    stack.pop();
                    stack.push(current);
                    current = current.right; // change root of subtree
                } else {
                    // visit
                    try {
                        consumer.accept(current);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        current = null;
                    }

                }
            }
        }
    }

}
