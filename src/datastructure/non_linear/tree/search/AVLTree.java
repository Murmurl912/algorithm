package datastructure.non_linear.tree.search;

import datastructure.non_linear.tree.BinarySearchTree;

import java.util.*;

/**
 * complete binary tree, is used as
 * search trees, cannot be updated after an insertion or deletion in time
 * less than Omega(n).
 * they are too rigid.
 *
 * the key to design of balanced search trees is the
 * introduction of glue, or degree of freedom.
 * this glue takes the from of red nodes in a red black tree, nodes
 * with 2 children(instead of three) in 2-3trees, and nodes with a height
 * imbalance of one in AVL trees.
 *
 * a complete binary search is an ideal binary tree
 * it takes o(logn) time of search
 * however is takes more time to keep binary search tree complete
 * after deletion or insertion
 *
 * though avl tree, red black tree may provide o(logn) time of
 * insertion, deletion and search.
 * as cache is about 50 times faster than memory
 *
 * AVL tree
 * height balanced tree
 *      balanced = abs(height(left child) - height(right child)) <= 1
 *
 * balanced binary search tree
 * @param <Key>
 * @param <Value>
 */
public class AVLTree<Key, Value> implements BinarySearchTree<Key, Value> {

    protected int size;
    protected Node<Pair<Key, Value>> root;
    protected final Comparator<Key> comparator;

    public AVLTree(Comparator<Key> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean insert(Key key, Value value) {
        Objects.requireNonNull(key);
        Node<Pair<Key, Value>> parent = search(key);
        Node<Pair<Key, Value>> node = insert(parent, key, value);
        if(node == null) {
            return false;
        }

        Node<Pair<Key, Value>> grandparent = node.parent == null ? null : node.parent.parent;
        while (grandparent != null && !rebalance(grandparent)) {
            grandparent = grandparent.parent;
        }
        return true;
    }

    @Override
    public boolean remove(Key key, Value value) {
        Objects.requireNonNull(key);
        Node<Pair<Key, Value>> node = search(key);
        if(node == null) {
            return false;
        }
        if(comparator.compare(key, node.data.key) != 0) {
            return false;
        }
        Node<Pair<Key, Value>> parent = delete(node);
        while (parent != null) {
            rebalance(parent);
            parent = parent.parent;
        }
        return true;
    }

    @Override
    public Value find(Key key) {
        if(root == null) {
            return null;
        }
        Node<Pair<Key, Value>> node = search(key);
        if(comparator.compare(node.data.key, key) == 0) {
            return node.data.value;
        } else {
            return null;
        }
    }


    /**
     * time complexity o(1)
     * clock wise rotate at center
     * before rotate:
     *        g (grand parent)
     *        |
     *        x (parent)
     *      /  \
     *     y    c
     *    / \
     *   a   b
     * after rotate:
     *        g (grand parent)
     *        |
     *        y
     *      /  \
     *     a      x
     *           / \
     *         b   c
     *
     * @param parent rotation center's parent
     */
    protected void zig(Node<Pair<Key, Value>> parent) {
        Node<Pair<Key, Value>> center = parent.left;
        parent.left = center.right;
        center.right = parent;

        // link parent
        center.parent = parent.parent;
        parent.parent = center;
        if(parent.left != null) {
            parent.left.parent = parent;
        }

        // link grand parent
        if(center.parent == null) {
            // parent is root
            root = center;
        } else if(center.parent.left == parent) {
            // parent is grandparent's left child
            center.parent.left = center;
        } else if(center.parent.right == parent) {
            // parent is grandparent's right child
            center.parent.right = center;
        }

        // update height
        updateHeight(parent);
    }

    /**
     * time complexity o(1)
     * counter clock wise rotate at center
     * before rotate:
     *        g (grand parent)
     *        |
     *        x (parent)
     *      /  \
     *     a      y
     *           / \
     *         b   c
     * after rotate:
     *        a (grand parent)
     *        |
     *        y
     *      /  \
     *     x    c
     *    / \
     *   a   b
     *@param parent rotation center's parent
     */
    protected void zag(Node<Pair<Key, Value>> parent) {
        Node<Pair<Key, Value>> center = parent.right;
        parent.right = center.left;
        center.left = parent;

        // link parent
        center.parent = parent.parent;
        parent.parent = center;
        if(parent.right != null) {
            parent.right.parent = parent;
        }

        // link grand parent
        if(center.parent == null) {
            // parent is root
            root = center;
        } else if(center.parent.left == parent) {
            // parent is grandparent's left child
            center.parent.left = center;
        } else if(center.parent.right == parent) {
            // parent is grandparent's right child
            center.parent.right = center;
        }

        // update height
        updateHeight(parent);
    }

    protected void updateHeight(Node<Pair<Key, Value>> node) {
        while (node != null) {
            node.height = Math.max(
                    node.left == null ? -1 : node.left.height,
                    node.right == null ? -1 : node.right.height
            ) + 1;
            node = node.parent;
        }
    }

    protected int balance(Node<Pair<Key, Value>> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    protected int height(Node<Pair<Key, Value>> node) {
        return node == null ? -1 : node.height;
    }

    protected boolean rebalance(Node<Pair<Key, Value>> node) {
        boolean flag = true;
        int factor = balance(node);
        if(factor > 1) {
            // left > right
            Node<Pair<Key, Value>> left = node.left;
            int compare = balance(left);
            if(compare >= 0) {
                // before:
                //               x(h+3) (node)
                //              /     \
                //            /        \
                //           y(h+2)     D(h)
                //         /   \
                //       /      \
                //      z(h+1)     C(h)
                //     /   \
                //   /      \
                //  [A]      [B]

                // after:
                //              y(h+2)
                //             /      \
                //           /         \
                //          z(h+1)      x(h+1)
                //         /   \       /   \
                //       /      \    /     \
                //      [A]   [B]   C(h)  D(h)
                zig(node);

            } else {
                //               x(h+3) (node)
                //              /     \
                //            /        \
                //           y(h+2)     D(h)
                //         /   \
                //       /      \
                //      [A]     z(h+1)
                //             /   \
                //           /      \
                //          [B]      [C]

                //               x(h+3)
                //              /     \
                //            /        \
                //           z(h+2)     D(h)
                //         /   \
                //       /      \
                //      y(h+1)  [C]
                //     /   \
                //   /      \
                //  [A]      [B]
                //
                zag(node.left);
                zig(node);
            }

        } else if(factor < -1) {
            // left < right
            Node<Pair<Key, Value>> right = node.right;
            int compare = balance(right);
            if(compare <= 0) {
                // before:
                //               z(h+2) (node)
                //              /     \
                //            /        \
                //           [D]       y(h+1)
                //                     /   \
                //                   /      \
                //                  [A]    x(h)
                //                         /   \
                //                       /      \
                //                     [B]      [C]
                // after:
                //                 y(h+2)
                //                /     \
                //              /        \
                //             z(h+1)    x(h)
                //            /  \       /   \
                //          /     \    /      \
                //         [D]   [A]  [B]      [C]
                // right.left > right.right
               zag(node);

            } else {
                // before:
                //               z(h+3) (node)
                //              /     \
                //            /        \
                //           [D]       y(h+2)
                //                     /    \
                //                   /       \
                //                  x(h+1)   [A]
                //                 /   \
                //               /      \
                //             [B]      [C]
                // after:
                //               z(h+3)
                //              /     \
                //            /        \
                //           [D]       x(h+2)
                //                    /    \
                //                  /       \
                //                 [B]      y(h+1)
                //                          /     \
                //                        /        \
                //                       [C]       [A]
                zig(node.right);
                zag(node);
            }
        } else {
            flag = false;
        }
        return flag;
    }

    protected Node<Pair<Key, Value>> insert(Node<Pair<Key, Value>> parent, Key key, Value value) {
        if(parent == null) {
            // insert as root
            root = new Node<>(new Pair<>(key, value),
                    parent, null, null, 0);
            size++;
            return root;
        }

        int compare = comparator.compare(parent.data.key, key);
        if(compare < 0) {
            // parent < node
            assert parent.right == null;
            parent.right = new Node<>(new Pair<>(key, value), parent, null, null, 0);
            size++;
            updateHeight(parent.right);
            return parent.right;
        } else if(compare > 0) {
            assert parent.left == null;
            parent.left = new Node<>(new Pair<>(key, value), parent, null, null, 0);
            size++;
            updateHeight(parent.left);
            return parent.left;
        } else {
            return null;
        }
    }

    protected Node<Pair<Key,Value>> delete(Node<Pair<Key, Value>> node) {
        if(node.left != null && node.right != null) {
            Node<Pair<Key, Value>> successor = successor(node);

            // swap node and its inorder successor
            Pair<Key, Value> data = successor.data;
            successor.data = node.data;
            node.data =  data;
            node = successor;
        }

        Node<Pair<Key, Value>> parent = node.parent;
        Node<Pair<Key, Value>> child = node.left == null
                ? node.right : node.left;
        if(parent == null)  {
            root = child;
        } else if(parent.left == node) {
            parent.left = child;
        } else if(parent.right == node) {
            parent.right = child;
        }

        // link parent
        if(child != null) {
            child.parent = parent;
        }

        node.parent = null;
        node.data = null;
        node.left = null;
        node.right = null;
        size--;
        if(parent != null) {
            updateHeight(parent);
        }
        return parent;
    }

    protected Node<Pair<Key, Value>> successor(Node<Pair<Key, Value>> node) {
        Node<Pair<Key, Value>> current = node.right;
        Node<Pair<Key, Value>> previous = null;
        while (current != null) {
            previous = current;
            current = current.left;
        }
        return previous;
    }

    protected Node<Pair<Key, Value>> search(Key key) {
        Node<Pair<Key, Value>> previous = null;
        for(Node<Pair<Key, Value>> node = root; node != null;) {
            int compareResult = comparator.compare(key, node.data.key);
            previous = node;
            if(compareResult > 0) {
                // key > node
                node = node.right;
            } else if(compareResult < 0) {
                node = node.left;
            } else {
                return node;
            }
        }
        return previous;
    }

    public Iterator<Pair<Key, Value>> inorder() {
        return new InOrderIterator(root);
    }

    public static class Pair<Key, Value> {
        protected Key key;
        protected Value value;

        public Pair(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "<" + key + ", " + value + ">";
        }
    }

    private boolean isTreeBalanced() {
        return false;
    }

    public static class Node<E> {
        protected E data;
        protected Node<E> parent;
        protected Node<E> left;
        protected Node<E> right;
        protected int height;

        protected Node(E data,
                       Node<E> parent,
                       Node<E> left,
                       Node<E> right,
                       int height) {
            this.data = data;
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.height = height;
        }

        protected Node<E> parent() {
            return parent;
        }

        protected void parent(Node<E> parent) {
            this.parent = parent;
        }

        protected Node<E> left() {
            return left;
        }

        protected void left(Node<E> left) {
            this.left = left;
        }

        protected Node<E> right() {
            return right;
        }

        protected void right(Node<E> right) {
            this.right = right;
        }

        protected E data() {
            return data;
        }

        protected void data(E data) {
            this.data = data;
        }

        protected int height() {
            return height;
        }

        protected void height(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    public class InOrderIterator implements Iterator<Pair<Key, Value>> {
        protected Deque<Node<Pair<Key, Value>>> stack;
        protected Node<Pair<Key, Value>> root;
        protected Node<Pair<Key, Value>> current;

        public InOrderIterator(Node<Pair<Key, Value>> root) {
            stack = new ArrayDeque<>();
            this.root = root;
            current = root;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty() || current != null;
        }

        @Override
        public Pair<Key, Value> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // go along left branch
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // stack is not empty and current is null
            Node<Pair<Key, Value>> node = stack.pop();
            current = node.right;

            return node.data;
        }
    }

    public static class Test {
        public static void main(String[] args) {
            AVLTree<Integer, Integer> tree = new AVLTree<>(Integer::compareTo);
            test(tree);
        }

        public static void test(AVLTree<Integer, Integer> tree) {
            Random random = new Random();
            int[] array = random.ints(100, 0, 100).toArray();
            for (int i : array) {
                tree.insert(i, i);
            }
            System.out.println("Random number: ");
            tree.inorder().forEachRemaining(System.out::println);
            for (int i : array) {
                if(i % 2 == 0) {
                    tree.remove(i, i);
                }
            }
            System.out.println("Random number: ");
            tree.inorder().forEachRemaining(System.out::println);
        }

    }
}
