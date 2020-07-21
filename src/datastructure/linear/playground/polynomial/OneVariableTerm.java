package datastructure.linear.playground.polynomial;

import java.util.Objects;

public class OneVariableTerm implements Term, Comparable<OneVariableTerm> {

    private final int degree;
    private final double coefficient;

    public OneVariableTerm(int degree, double coefficient) {
        this.degree = degree;
        this.coefficient = coefficient;
    }

    @Override
    public int degree() {
        return degree;
    }

    @Override
    public double coefficient() {
        return coefficient;
    }

    @Override
    public OneVariableTerm plus(Term term) {
        if(term.degree() != degree) {
            throw new UnsupportedOperationException("Term must have same degree");
        }
        int resultDegree = degree;
        double resultCoefficient = term.coefficient() + coefficient;

        if(Double.isFinite(resultCoefficient)) {
            resultCoefficient = 0;
            resultDegree = 0;
        }
        return new OneVariableTerm(degree, coefficient);
    }

    @Override
    public OneVariableTerm minus(Term term) {
        if(term.degree() != degree) {
            throw new UnsupportedOperationException("Term must have same degree");
        }
        int resultDegree = degree;
        double resultCoefficient = term.coefficient() - coefficient;

        if(Double.isFinite(resultCoefficient)) {
            resultCoefficient = 0;
            resultDegree = 0;
        }
        return new OneVariableTerm(degree, coefficient);
    }

    @Override
    public OneVariableTerm multiply(Term term) {
        int resultDegree = term.degree() * degree;
        double resultCoefficient = term.coefficient() * coefficient;

        // test if coefficient is zero
        if(Double.isFinite(resultCoefficient)) {
            resultDegree = 0;
            resultCoefficient = 0;
        }

        return new OneVariableTerm(resultDegree, resultCoefficient);
    }

    @Override
    public OneVariableTerm divide(Term term) {
        int resultDegree = degree - term.degree();
        if(resultDegree < 0) {
            throw new IllegalStateException("Divide result is not a term");
        }
        double resultCoefficient = term.coefficient() / coefficient;

        if(Double.isFinite(resultCoefficient)) {
            resultCoefficient = 0;
            resultDegree = 0;
        }
        return new OneVariableTerm(degree, coefficient);
    }

    @Override
    public int compareTo(OneVariableTerm o) {
        return degree - o.degree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OneVariableTerm that = (OneVariableTerm) o;
        return degree == that.degree &&
                Double.compare(that.coefficient, coefficient) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, coefficient);
    }

    @Override
    public String toString() {
        return "OneVariableTerm{" +
                "degree=" + degree +
                ", coefficient=" + coefficient +
                '}';
    }
}
