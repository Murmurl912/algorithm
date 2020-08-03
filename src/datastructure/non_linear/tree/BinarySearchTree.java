package datastructure.non_linear.tree;

public interface BinarySearchTree<Key, Value> {

    public int size();

    public boolean insert(Key key, Value value);

    public boolean remove(Key key, Value value);

    public Value find(Key key);

}
