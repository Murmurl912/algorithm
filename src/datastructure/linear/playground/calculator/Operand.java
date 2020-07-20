package datastructure.linear.playground.calculator;

public class Operand implements Token {
    private String token;
    private double value;

    public Operand(String token) {
        value = Double.parseDouble(token);
    }

    @Override
    public String getTokenString() {
        return token;
    }

    @Override
    public void setTokenString(String token) {
        this.token = token;
        value = Double.parseDouble(token);
    }

    public double getValue() {
        return value;
    }

}
