package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;


public class LlistaModificada<E extends Comparable<E>> extends ILlistaAbstracta<E> {

    //Add new element to the list, at the end.
    public void inserir(E e) {
        //Hem de crear un add Node
        Node<E> add = new Node<E>(e); // "Guardem" (punters) la persona a add
        Node <E> current;
        Node<E> previous;
        current = ghost.next;
        previous = ghost.next;
        if(esBuida()){
            //Insertion af the begining because of empty list
            ghost.next = add;
            numElem++;
        }
        else{
            while(current != null && e.compareTo(current.info) != 0){
                //There new element was not already inside the list
                previous = current;
                current = current.next;
            }
            if(current == null){
                //End of the list, insertion at the end of it
                add.next = previous.next;
                previous.next = add;
                numElem++;
            }
            else{
                previous.next = add;
            }
        }
    }

    public void esborrar(E e) throws ElementNoTrobat{
        Node<E> current = ghost.next;
        Node<E> previous = ghost;

        while(current != null && current.info.compareTo(e) != 0){
            previous = current;
            current = current.next;
        }
        if(current == null ){
            //not found
            throw new ElementNoTrobat();
        }
        //Esborrrar i arreglar nodes
        previous.next = current.next;
        numElem--;
    }

    //get position in which given element is located in the list
    public int buscar(E e) throws ElementNoTrobat {
        int i = 0;
        Node<E> consultador = ghost.next;

        //Com LlistaNoOrdenada, hem de recórrer tots els elements o fins trobar
        while(consultador.info.compareTo(e) != 0 && consultador.next != null){
            consultador = consultador.next;
            i++;
        }
        if (consultador.info.compareTo(e) == 0){
            return i;
        } else{
            throw new ElementNoTrobat();
        }

    }

    public boolean existeix(E e){
        try {
            this.buscar(e);
            return true;
        } catch (ElementNoTrobat error) {
            return false;
        }
    }
}
