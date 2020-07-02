package datastructure.linear.playground.sparse_array;

public interface KeyValuePair<Key, Value> {
    public void setKey(Key key);

    public void setValue(Value value);

    public Key getKey();

    public Value getValue();
}
