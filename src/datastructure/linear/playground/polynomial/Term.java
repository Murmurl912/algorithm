package datastructure.linear.playground.polynomial;

public interface Term {

    public int degree();

    public double coefficient();

    public Term plus(Term term);

    public Term minus(Term term);

    public Term multiply(Term term);

    public Term divide(Term term);
}
