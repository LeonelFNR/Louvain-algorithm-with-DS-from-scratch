package cat.urv.deim;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;

public abstract class ILlistaAbstracta<E extends Comparable<E>> implements ILlistaGenerica<E>{
    protected int numElem;
    protected Node<E> ghost;


    //Builder
    public ILlistaAbstracta(){
        this.numElem = 0;
        this.ghost = new Node<E>(null);
        this.ghost.next = null;
    }

    //Functions that depend on memory allocation (static or dynamic)
    public abstract void inserir(E e);
    public abstract boolean existeix(E e);
    public abstract void esborrar(E e) throws ElementNoTrobat;
    public abstract int buscar(E e) throws ElementNoTrobat;

    //Generalizable funcitons
    public E consultar(int pos) throws PosicioForaRang{
        if(pos >= numElements() || pos < 0){
            throw new PosicioForaRang();
        } else{
            Node<E> explorer = ghost.next;
            for(int i = 0; i < pos; i++){
                explorer = explorer.next;
            }
            return explorer.info;
        }
    }
    public boolean esBuida(){
        return numElem == 0;
    }

    public int numElements(){
        return numElem;
    }

    public Iterator<E> iterator(){
        return new ListIterator();
    }

    public class ListIterator implements Iterator<E>{
       protected Node<E> element;
       protected E info;

       public ListIterator(){
        element = ghost.next;
       }

       @Override
       public boolean hasNext(){
        //this function returns if the next element exists
        return element != null;
       }

       @Override
       public E next(){
        if(hasNext()){
            info = element.info;
            element = element.next;
            return info;

        } else{
            throw new NoSuchElementException();
        }
       }

    }
}
