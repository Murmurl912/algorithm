package datastructure.non_linear.graph;

public interface Graph<V, E> {
    // adjacency matrix

    // incidence matrix

    public class Vertex<V> {
        protected V data;

        public Vertex(V data) {
            this.data = data;
        }

        public void setData(V data) {
            this.data = data;
        }

        public V getData() {
            return data;
        }
    }

    public class Edge<E> {
        protected E data;

        public Edge(E data) {
            this.data = data;
        }

        public void setData(E data) {
            this.data = data;
        }

        public E getData() {
            return data;
        }
    }
}
