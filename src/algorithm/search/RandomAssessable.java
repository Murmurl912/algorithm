package algorithm.search;

public interface RandomAssessable<Element, Index> {

    public Element elementAt(Index index);

    public Index indexOf(Element element, Index from, Index to);

}
