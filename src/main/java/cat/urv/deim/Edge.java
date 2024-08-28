package cat.urv.deim;

public class Edge<V extends Comparable <V>,E> implements Comparable<Edge<V,E>> {
    private V vertex; //the other vertex of the edge, here will go the KEY!!!, do not confuse with info!!!
    private E weight;

    public Edge(V vertex, E weight){
        this.vertex = vertex;
        this.weight = weight;
    }

    public void setVertex(V vertex){
        this.vertex = vertex;
    }

    public void setWeight(E weight){
        this.weight = weight;
    }

    public V getVertexKey(){
        return this.vertex;
    }

    public E getWeight(){
        return this.weight;
    }

    @Override
    public int compareTo(Edge<V,E> other){
        return this.getVertexKey().compareTo(other.getVertexKey());
    }

}
