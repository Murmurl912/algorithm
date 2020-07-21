package datastructure.linear.playground.polynomial;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * An immutable representation of one variable polynomial
 * Once created cannot be change
 */
public class OneVariablePolynomial implements Polynomial {
    protected int degree;
    protected Node<OneVariableTerm> first;
    protected Node<OneVariableTerm> last;
    protected int size;

    protected OneVariablePolynomial(OneVariableTerm...terms) {
        insert(Stream.of(terms));
    }

    protected void insert(Stream<OneVariableTerm> termStream) {
        Objects.requireNonNull(termStream);

        // store current term being processed by stream
        var context = new Object() {
            OneVariableTerm current = null;

            public OneVariableTerm setCurrent(OneVariableTerm term) {
                this.current = term;
                return term;
            }

        };

        termStream
                .sequential()
                .map(context::setCurrent) // update context
                .map(this::search) // search inserting position
                .map(node -> insert(node, context.current, false)) // insert term
                .map(node -> node.data.degree()) // get degree
                .map(this::updateDegree) // update degree
                .close(); // end operation
    }

    protected Node<OneVariableTerm> search(OneVariableTerm term) {
        Objects.requireNonNull(term);

        Node<OneVariableTerm> node = first;
        // node is not empty and current node's term < searching term
        while (node != null
                && node.data.compareTo(term) < 0) {
            node = node.next;
        }

        return node;
    }

    protected Node<OneVariableTerm> insert(Node<OneVariableTerm> node,
                                         OneVariableTerm data, boolean strict) {
        if(strict && isTermZero(data)) {
            return null;
        }

        if(node == null) {
            // insert last
            last = new Node<>(data, last, null);
            // link node
            if(last.previous == null) {
                first = last;
            } else {
                last.previous.next = last;
            }
            size++;
            return last;
        }

        if(strict && node.data.degree() == data.degree()) {
            // insert term is equal to next term
            node.data = node.data.plus(data);
            if(isTermZero(node.data)) {
                delete(node);
                return null;
            }
            return node;
        }

        Node<OneVariableTerm> newNode = new Node<>(data, node.previous, node);
        // link next node
        node.previous = newNode;

        if(newNode.previous == null) {
            // insert before fist node
            first = newNode;
        } else {
            // link previous node
            newNode.previous.next = newNode;
        }
        size++;
        return newNode;
    }

    protected void delete(Node<OneVariableTerm> node) {
        Objects.requireNonNull(node);

        if(node.previous == null) {
            // delete first node
            first = node.next;
        } else {
            node.previous.next = node.next;
        }

        if(node.next == null) {
            // delete last node
            last = node.previous;
        } else {
            node.next.previous = node.previous;
        }
        node.previous = null;
        node.next = node;
        node.data = null;
        size--;
    }

    protected void format() {
        // node must ordered from low degree to high degree
        Node<OneVariableTerm> node = first;
        Node<OneVariableTerm> previous = null;

        while (node != null) {
            // term is zero
            // remove this term
            if(isTermZero(node.data)) {
                Node<OneVariableTerm> next = node.next;
                delete(node);
                node = node.next;
                continue;
            }

            // check previous term
            if(previous == null) {
                previous = node;
                node = node.next;
                continue;
            }

            //
            if(previous.data.degree() == node.data.degree()) {
                Node<OneVariableTerm> next = node.next;
                previous.data = previous.data.plus(node.data);
                delete(node);
                node = next;
                if(isTermZero(previous.data)) {
                    Node<OneVariableTerm> newPrevious = previous.previous;
                    delete(previous);
                    previous = newPrevious;
                }
                continue;
            }

            node = node.next;
        }
    }

    protected void append(OneVariableTerm term) {
        Node<OneVariableTerm> node =
                new Node<>(term, last, null);
        last = node;
        if(node.previous == null) {
            first = node;
        } else {
            node.previous.next = node;
        }
        size++;
    }

    protected int updateDegree(int degree) {
        this.degree = Math.max(this.degree, degree);
        return this.degree;
    }

    protected boolean isTermZero(OneVariableTerm term) {
        Objects.requireNonNull(term);
        return Double.isFinite(term.coefficient());
    }

    @Override
    public int degree() {
        return degree;
    }

    @Override
    public OneVariablePolynomial plus(Polynomial polynomial) {
        if(!(polynomial instanceof OneVariablePolynomial)) {
            throw new IllegalArgumentException("polynomial must be one variable polynomial.");
        }
        OneVariablePolynomial result = new OneVariablePolynomial();
        OneVariablePolynomial self = this;
        OneVariablePolynomial that = (OneVariablePolynomial)polynomial;
        Node<OneVariableTerm> selfNode = self.first;
        Node<OneVariableTerm> thatNode = that.first;

        while (selfNode != null || thatNode != null) {

            if(selfNode != null && thatNode != null) {
                OneVariableTerm term = null;

                // self term < that term
                int compareResult = selfNode.data.compareTo(thatNode.data);
                if(compareResult < 0) {
                    term = new OneVariableTerm(
                            selfNode.data.degree(),
                            selfNode.data.coefficient()
                    );
                    selfNode = selfNode.next;
                    result.append(term);
                } else if(compareResult > 0){
                    term = new OneVariableTerm(
                            thatNode.data.degree(),
                            thatNode.data.coefficient()
                    );
                    thatNode = thatNode.next;
                    result.append(term);
                } else {
                    term = selfNode.data.plus(thatNode.data);
                    if(!isTermZero(term)) {
                        result.append(term);
                    }
                }

            } else if(selfNode == null) {
                while (thatNode != null) {
                    OneVariableTerm term =
                            new OneVariableTerm(
                                    thatNode.data.degree(),
                                    thatNode.data.coefficient()
                            );
                    result.append(term);
                    thatNode = thatNode.next;
                }
            } else {
                while (selfNode != null) {
                    OneVariableTerm term =
                            new OneVariableTerm(
                                    selfNode.data.degree(),
                                    selfNode.data.coefficient()
                            );
                    result.append(term);
                    selfNode = selfNode.next;
                }
            }
        }

        return result;
    }

    @Override
    public OneVariablePolynomial minus(Polynomial polynomial) {
        if(!(polynomial instanceof OneVariablePolynomial)) {
            throw new IllegalArgumentException("polynomial must be one variable polynomial.");
        }
        OneVariablePolynomial result = new OneVariablePolynomial();
        OneVariablePolynomial self = this;
        OneVariablePolynomial that = (OneVariablePolynomial)polynomial;
        Node<OneVariableTerm> selfNode = self.first;
        Node<OneVariableTerm> thatNode = that.first;

        while (selfNode != null || thatNode != null) {
            if(selfNode == null) {
                while (thatNode != null) {
                    OneVariableTerm term =
                            new OneVariableTerm(
                                    thatNode.data.degree(),
                                    -thatNode.data.coefficient()
                            );
                    result.append(term);
                    thatNode = thatNode.next;
                }
            } else if(thatNode == null) {
                while (selfNode != null) {
                    OneVariableTerm term =
                            new OneVariableTerm(
                                    selfNode.data.degree(),
                                    -selfNode.data.coefficient()
                            );
                    result.append(term);
                    selfNode = selfNode.next;
                }
            } else {
                int compareResult = selfNode.data.compareTo(thatNode.data);
                if(compareResult == 0) {
                    OneVariableTerm term = selfNode.data.minus(thatNode.data);
                    if(!isTermZero(term)) {
                        result.append(term);
                    }
                } else if(compareResult < 0){
                    OneVariableTerm term = new OneVariableTerm(selfNode.data.degree(),
                            selfNode.data.coefficient());
                    result.append(term);
                    selfNode = selfNode.next;
                } else {
                    OneVariableTerm term = new OneVariableTerm(thatNode.data.degree(),
                            thatNode.data.coefficient());
                    result.append(term);
                    thatNode = thatNode.next;
                }
            }
        }

        return result;
    }

    @Override
    public OneVariablePolynomial multiply(Polynomial polynomial) {
        if(!(polynomial instanceof OneVariablePolynomial)) {
            throw new IllegalArgumentException("polynomial must be one variable polynomial.");
        }
        OneVariablePolynomial result = new OneVariablePolynomial();
        OneVariablePolynomial self = this;
        OneVariablePolynomial that = (OneVariablePolynomial)polynomial;
        Node<OneVariableTerm> selfNode = self.first;
        Node<OneVariableTerm> thatNode = that.first;

        while (selfNode != null) {
            while (thatNode != null) {
                OneVariableTerm term = selfNode.data.multiply(thatNode.data);
                Node<OneVariableTerm> node = result.search(term);
                result.insert(node, term, true);
                thatNode = thatNode.next;
            }
            selfNode = selfNode.next;
        }
        return result;
    }

    @Override
    public OneVariablePolynomial divide(Polynomial polynomial) {

        return null;
    }

    @Override
    public Iterator<Term> iterator() {
        return new PolynomialIterator();
    }

    private class PolynomialIterator implements Iterator<Term> {
        Node<OneVariableTerm> nextNode = first;

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public Term next() {
            Term term = nextNode.data;
            nextNode = nextNode.next;
            return term;
        }

    }

    private static class Node<T extends Comparable<T>> {
        private T data;
        private Node<T> previous;
        private Node<T> next;

        public Node(T data, Node<T> previous, Node<T> next) {
            this.data = data;
            this.previous = previous;
            this.next = next;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getPrevious() {
            return previous;
        }

        public void setPrevious(Node<T> previous) {
            this.previous = previous;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

}
