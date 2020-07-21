package datastructure.linear.playground.polynomial;

public interface Polynomial extends Iterable<Term> {

    public int degree();

    public Polynomial plus(Polynomial polynomial);

    public Polynomial minus(Polynomial polynomial);

    public Polynomial multiply(Polynomial polynomial);

    public Polynomial divide(Polynomial polynomial);

}
