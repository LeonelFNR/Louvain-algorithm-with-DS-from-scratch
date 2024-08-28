package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;

public class LlistaOrdenada<E extends Comparable<E>> extends ILlistaAbstracta<E> {
    //Functions that depend on memory allocation (static or dynamic)
    public void inserir(E e){
        Node<E> add = new Node<E>(e);
        Node<E> current = ghost.next;
        Node<E> previous = ghost;

        while(current != null && current.info.compareTo(e) < 0){
            previous = current;
            current = current.next;
        }
        if(current == null){
            //element goes in the last position
            previous.next = add;
            numElem++;
        }
        else if(current.info.compareTo(e) == 0){
            //we just overwrite info in case the element is updated or repeated
            current.info = e;
        }
        else{
            //element goes in a middle position
            add.next = current;
            previous.next = add;
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

    public void esborrar(E e) throws ElementNoTrobat{
        try {
            int pos = buscar(e);
            Node<E> current = ghost.next;
            Node<E> previous = ghost;
            for(int i = 0; i < pos; i++){
                previous = current;
                current = current.next;
            }
            previous.next = current.next;
            numElem--;
        } catch (ElementNoTrobat error) {
            throw new ElementNoTrobat();
        }
    }
    public int buscar(E e) throws ElementNoTrobat{
        if(esBuida()){
            throw new ElementNoTrobat();
        } else{
            int pos = 0;
            Node<E> explorer = ghost.next;
            while(explorer.next!= null & e.compareTo(explorer.info) > 0 && pos < numElem){
                pos++;
                explorer = explorer.next;
            }
            if(explorer == null || explorer.info.compareTo(e) !=0 ){
                throw new ElementNoTrobat();
            }
            else{
                return pos;
            }
        }
    }
}
