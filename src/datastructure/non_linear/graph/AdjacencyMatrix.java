package datastructure.non_linear.graph;

import java.util.ArrayList;

public class AdjacencyMatrix<V, E> implements Graph<V, E> {

    protected ArrayList<Vertex<V>> vertexes;
    protected ArrayList<ArrayList<Edge<E>>> matrix;

    }
