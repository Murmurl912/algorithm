package datastructure.linear.playground.calculator.token;

import datastructure.linear.playground.calculator.Token;

public interface StringToken extends Token {
    @Override
    default TokenType getTokenType() {
        return TokenType.STRING;
    }
}
