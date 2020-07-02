package datastructure.linear.playground.sparse_array;

public class IntKeyPair<Value> implements KeyValuePair<Integer, Value> {
    private Value value;
    private Integer key;

    @Override
    public void setKey(Integer integer) {
        this.key = integer;
    }

    @Override
    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public Value getValue() {
        return value;
    }
}
