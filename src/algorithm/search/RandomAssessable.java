package algorithm.search;

/**
 * an interface mark a class can be random accessed by index
 * @param <Element> type of data stores in class
 * @param <Index> index type
 */
public interface RandomAssessable<Element, Index> {

    /**
     * get element by index
     * @param index index of element to retrieve
     * @return element at given index
     */
    public Element elementAt(Index index);

    /**
     * search index of element in given index range
     * @param element element to search for
     * @param from where search begin
     * @param to where search end, not include end itself
     * @return index of searching element
     */
    public Index indexOf(Element element, Index from, Index to);
}
