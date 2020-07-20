package datastructure.linear.playground.calculator.token;

import datastructure.linear.playground.calculator.Token;

public interface NumericToken extends Token {

    public double parse();

    @Override
    default TokenType getTokenType() {
        return TokenType.NUMERIC;
    }
}
