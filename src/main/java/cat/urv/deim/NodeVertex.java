package cat.urv.deim;

public class NodeVertex<L extends Comparable <L>, M extends Comparable<M>, Q extends Comparable<Q>> implements Comparable<NodeVertex<L,M,Q>> {
    //node class that contains the vertices
    //it conatins the vertex info and
    //the adjacencies of "higher" vertices

    private L key;
    private M info;
    public LlistaOrdenada<Q> higherAdj;

    public NodeVertex(L key, M info){
        this.key = key;
        this.info = info;
        this.higherAdj = new LlistaOrdenada<Q>();
    }

    public L getKey(){
        return this.key;
    }

    public M getInfo(){
        return this.info;
    }

    public void setInfo(M newinfo){
        this.info = newinfo;
    }

    @Override
    public int compareTo(NodeVertex<L,M,Q> other){
        return this.getKey().compareTo(other.getKey());
    }

}
