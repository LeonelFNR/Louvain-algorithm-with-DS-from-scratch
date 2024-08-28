package cat.urv.deim;


import cat.urv.deim.exceptions.ElementNoTrobat;

public class LlistaNoOrdenada<E extends Comparable<E>> extends ILlistaAbstracta<E> {
    //Functions that depend on memory allocation (static or dynamic)
    public void inserir(E e){
        //if an element is repeated, it won't be added again

        try {
            int pos = buscar(e);
            Node<E> explorer = ghost.next;

            for(int i = 0; i < pos; i++){
                explorer = explorer.next;
            }
            explorer.info = e;
        } catch (ElementNoTrobat error) {
            Node<E> add = new Node<E>(e);
            add.next = ghost.next;
            ghost.next = add;
            numElem++;
        }

    }
    public boolean existeix(E e){
        try {
            buscar(e);
            return true;
        } catch (ElementNoTrobat error) {
            return false;
        }
    }
    public  void esborrar(E e) throws ElementNoTrobat{
        Node<E> current = ghost.next;
        Node<E> previous = ghost;

        while(current != null && current.info.compareTo(e) !=0){
            previous = current;
            current = current.next;
        }
        if(current == null){
            throw new ElementNoTrobat();
        }else{
            previous.next = current.next;
            numElem--;
        }
    }
    public  int buscar(E e) throws ElementNoTrobat{
        Node<E> explorer = ghost.next;
        int pos = 0;

        while(explorer != null && explorer.info.compareTo(e) != 0){
            pos++;
            explorer = explorer.next;
        }
        if(explorer == null){
            throw new ElementNoTrobat();
        } else{
            return pos;
        }
    }


}
