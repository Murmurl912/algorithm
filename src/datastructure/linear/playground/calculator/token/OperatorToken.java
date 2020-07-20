package datastructure.linear.playground.calculator.token;

import datastructure.linear.playground.calculator.Token;

public interface OperatorToken extends Token {
    @Override
    default TokenType getTokenType() {
        return TokenType.OPERATOR;
    }
}
