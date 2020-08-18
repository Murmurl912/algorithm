package datastructure.container;

import java.util.Comparator;

public interface Sortable<E> {
    public void sort(boolean isAscend, Comparator<E> comparator);
}
