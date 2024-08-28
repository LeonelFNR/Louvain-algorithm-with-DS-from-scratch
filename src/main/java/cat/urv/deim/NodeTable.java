package cat.urv.deim;



public class NodeTable<K extends Comparable<K>,V> {

    //NodeHash content
    public LlistaOrdenada<NodeHash<K, V>> nodeList;

    public NodeTable(){
        this.nodeList = new LlistaOrdenada<NodeHash<K,V>>();
    }

}
