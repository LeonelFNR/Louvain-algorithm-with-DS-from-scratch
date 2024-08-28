package cat.urv.deim;

public class Node<E extends Comparable<E>> {
    E info;
    Node<E> next;

    //Builder of the variable
    public Node (E p){
        this.info = p;
        this.next = null;
    }
}
