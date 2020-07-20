package datastructure.linear.playground;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * a simple rpn based calculator
 * cannot compute expression contain +number or -number
 * such as: - 9 + 1, 9 + 10 * (- 10 + 2), +10 * 12, 1 ----- 1
 * - and + operator contain override
 * need to improve tokenizer
 * an context free tokenizer cannot
 * determine - is minus operator or negative operator
 *
 * more commonly determine whether - is subtraction mark or negative mark
 * is the job of syntax analyzer which builds a abstract syntax tree
 *
 * using an AST to calculate math expression is beyond
 * usage of linear data structure
 */
@Deprecated
public class Calculator {

    public ArrayList<Token> tokenize(String expression) {
        expression = expression.strip();
        StringBuilder buffer = new StringBuilder();
        ArrayList<Token> tokens = new ArrayList<>();
        expression.chars()
                .filter(integer -> !(integer == '\t'
                        || integer == '\n'
                        || integer == '\r'
                        || integer == ' '
                        || integer == '\b'
                        || integer == '\f')
                )
                .forEachOrdered(integer -> {
                    boolean delimiter = false;
                    switch (integer) {
                        case '+':
                            tokens.add(Operator.OPERATOR_PLUS);
                            delimiter = true;
                            break;
                        case '/':
                            tokens.add(Operator.OPERATOR_DIVISION);
                            delimiter = true;
                            break;
                        case '*':
                            tokens.add(Operator.OPERATOR_MULTIPLICATION);
                            delimiter = true;
                            break;
                        case '^':
                            tokens.add(Operator.OPERATOR_POWER);
                            delimiter = true;
                            break;
                        case '-':
                            tokens.add(Operator.OPERATOR_MINUS);
                            delimiter = true;
                            break;
                        case '(':
                            tokens.add(Operator.OPERATOR_LEFT_BRACKET);
                            delimiter = true;
                            break;
                        case ')':
                            tokens.add(Operator.OPERATOR_RIGHT_BRACKET);
                            delimiter = true;
                            break;
                        default:
                            buffer.append((char)integer);
                    }

                    if(delimiter) {
                        String token = buffer.toString();
                        buffer.delete(0, buffer.length());
                        if(!token.isEmpty() && !token.isBlank()) {
                            tokens.add(tokens.size() - 1,
                                    new Operand(token));
                        }
                    }
                });

        if(buffer.length() > 0) {
            String token = buffer.toString();
            if(!token.isBlank()) {
                tokens.add(new Operand(token));
            }
        }
        return tokens;
    }

    /**
     * suppose to determine + and - override
     * @param tokens list of token
     * @return parsed token
     */
    public List<Token> parse(List<Token> tokens) {
        Token[] previous = new Token[1];

        tokens.forEach(token -> {

        });

        return tokens;
    }

    public ArrayList<Token> rpn(List<Token> tokens) {
        ArrayList<Token> operands = new ArrayList<>();
        ArrayDeque<Operator> operators = new ArrayDeque<>();
        Token[] previous = new Token[1];

        tokens.forEach(token -> {
            // operator
            if(token instanceof Operator) {
                Operator operator = (Operator) token;

                // left bracket
                if(operator == Operator.OPERATOR_LEFT_BRACKET) {
                    operators.push(operator);
                    return;
                }

                // right bracket
                if(operator == Operator.OPERATOR_RIGHT_BRACKET) {
                    while (!operators.isEmpty()
                            && operators.peek() != Operator.OPERATOR_LEFT_BRACKET) {
                       operands.add(operators.pop());
                    }
                    if(operators.isEmpty()
                            || operators.peek() != Operator.OPERATOR_LEFT_BRACKET) {
                        throw new IllegalStateException("Unmatched left bracket");
                    }
                    operators.pop();
                    return;
                }

                // ensure operator stack ordered correctly
                // in rpn expression the first two operands
                // next to operator is calculate first
                // in which the operator stack is ordered from
                // low priority to high
                while (!operators.isEmpty()
                        && operator.priority <=
                        operators.peek().priority) {
                    operands.add(operators.pop());
                }

                operators.push(operator);
                return;
            }

            // operands
            Operand operand = (Operand) token;
            operands.add(operand);

        });

        while (!operators.isEmpty()) {
            operands.add(operators.pop());
        }
        return operands;
    }

    // cannot compute negative operator
    public double compute(ArrayList<Token> expression) {
        ArrayDeque<Operand> operands = new ArrayDeque<>();
        expression.forEach(token -> {
            if(token instanceof Operand) {
                operands.push((Operand)token);
                return;
            }

            if(token instanceof Operator) {
                Operator operator = (Operator)token;
                int operandCount = operator.operands;
                ArrayList<Operand> parameters = new ArrayList<>();
                while (operandCount-- > 0) {
                    parameters.add(0, operands.isEmpty() ? new Operand(0) : operands.pop());
                }
                double value = operator.evaluate(parameters.toArray(new Operand[]{}));
                Operand operand = new Operand(value);
                operands.push(operand);
                return;
            }

            throw new IllegalStateException("Token is not a operand or operator");
        });
        if(operands.size() != 1) {
            throw new IllegalStateException("Expresion is invalid.");
        }
        return operands.pop().number.doubleValue();
    }

    public static interface Token {

    }

    public static enum Operator implements Token{
        OPERATOR_PLUS("+", 2, 1),
        OPERATOR_MINUS("-", 2, 1),
        OPERATOR_MULTIPLICATION("*", 2, 2),
        OPERATOR_DIVISION("/", 2, 2),
        OPERATOR_POWER("^", 2, 2),
        // a bracket will change operator priority inside bracket enclosure
        OPERATOR_LEFT_BRACKET("(", 0, 0), // priority change
        OPERATOR_RIGHT_BRACKET(")", 0, 0); // priority change

        private final int priority;
        private final String name;
        private final int operands;

        Operator(String name, int operands, int priority) {
            this.priority = priority;
            this.name = name;
            this.operands = operands;
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

        public double evaluate(Operand...operands) {
            assert operands.length == this.operands : this + " require " + this.operands + " operand(s)";
            double value = 0;
            switch (this) {
                case OPERATOR_PLUS:
                    value = operands[0].number.doubleValue() + operands[1].number.doubleValue();
                    break;
                case OPERATOR_MINUS:
                    value = operands[0].number.doubleValue() - operands[1].number.doubleValue();
                    break;
                case OPERATOR_POWER:
                    value = Math.pow(operands[0].number.doubleValue(), operands[1].number.doubleValue());
                    break;
                case OPERATOR_DIVISION:
                    value = operands[0].number.doubleValue() / operands[1].number.doubleValue();
                    break;
                case OPERATOR_MULTIPLICATION:
                    value = operands[0].number.doubleValue() * operands[1].number.doubleValue();
                    break;
                default:
                    throw new UnsupportedOperationException(this + " operator is not support.");
            }
            return value;

        }

    }

    public static class Operand implements Token {
        private String token;
        private Number number;

        public Operand(Number number) {
            this.number = number;
            token = number.toString();
        }

        public Operand(String token, Number number) {
            this.number = number;
            this.token = token;
        }

        public Operand(String number) {
            this.number = Double.valueOf(number);
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Number getNumber() {
            return number;
        }

        public void setNumber(Number number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return number.toString();
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println("Tokenize: ");
        ArrayList<Token> tokens = calculator.tokenize("-(2 * (9 + 6 / 3 - 5) + 4)");
        tokens.forEach(System.out::println);
        System.out.println("RPN: ");
        ArrayList<Token> operands = calculator.rpn(tokens);
        operands.forEach(System.out::println);
        System.out.println("Evaluate: ");
        System.out.println(calculator.compute(operands));
    }
}
