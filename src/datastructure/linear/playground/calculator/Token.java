package datastructure.linear.playground.calculator;

public interface Token {

    public String getTokenString();

    public TokenType getTokenType();

    public static enum  TokenType {
        NUMERIC, // such as 1234 1e234
        KEYWORD, // depends on user input
        IDENTIFIER, // standard identifier
        STRING, // quoted string
        OPERATOR, // general math operator and programing operator
        SYMBOL, // brackets comma etc.
    }
}
