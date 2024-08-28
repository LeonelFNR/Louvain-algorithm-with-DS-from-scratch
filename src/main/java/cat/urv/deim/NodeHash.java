package cat.urv.deim;

public class NodeHash<K extends Comparable<K>,V> implements Comparable<NodeHash<K,V>> {

    //NodeHash content
    private K key;
    private V value;
    private NodeHash<K,V> next;


    //Builder
    public NodeHash(K key, V value){
        this.key = key;
        this.value = value;
        this.next = null;
    }

    public K getKey(){
        return this.key;
    }
    public V getValue(){
        return this.value;
    }

    public NodeHash<K,V> getNext(){
        return this.next;
    }

    public void setValue(V value){
        this.value = value;
    }

    public void setNext(NodeHash<K,V> following){
        this.next = following;
    }

    @Override
    public int compareTo(NodeHash<K,V> p){
        return this.getKey().compareTo(p.getKey());
    }

}
