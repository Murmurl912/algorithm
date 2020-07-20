package datastructure.linear.playground.calculator;

public class Operator implements Token {
    private final OperatorType type;
    private int priority;

    public Operator(OperatorType type) {
        this.type = type;
        priority = type.priority;
    }

    public Operator(OperatorType type, int priority) {
        this(type);
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public OperatorType getType() {
        return type;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String getTokenString() {
        return type.name;
    }

    @Override
    public void setTokenString(String token) {
        throw new UnsupportedOperationException();
    }

    public static enum  OperatorType {
        OPERATOR_PLUS("+", 1),
        OPERATOR_MINUS("-", 1),
        OPERATOR_MULTIPLICATION("*", 2),
        OPERATOR_DIVISION("/", 2),
        OPERATOR_POWER("^", 2),
        // a bracket will change operator priority inside bracket enclosure
        OPERATOR_LEFT_BRACKET("(", 0),
        OPERATOR_RIGHT_BRACKET(")", 0);

        private final int priority;
        private final String name;

        OperatorType(String name, int priority) {
            this.priority = priority;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public int getPriority() {
            return priority;
        }

        public String getName() {
            return name;
        }

    }
}
