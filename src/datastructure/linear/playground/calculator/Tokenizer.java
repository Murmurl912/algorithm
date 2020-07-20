package datastructure.linear.playground.calculator;

import java.io.StreamTokenizer;
import java.util.HashSet;
import java.util.Set;

/**
 * expression like mathematical usually contain
 * whitespace , numeric, alphabetic and some separator
 * such as bracket and comma characters
 *
 * a lexer is an advance tokenizer
 * @link https://rosettacode.org/wiki/Compiler/lexical_analyzer
 *
 */
public interface Tokenizer {

    public Token next();

    public boolean hasNext();

}
