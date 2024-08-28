package cat.urv.deim;

import cat.urv.deim.exceptions.*;
import java.util.Iterator;

public interface ILlistaGenerica<E> {
    public void inserir(E e);
    public void esborrar(E e) throws ElementNoTrobat;
    public E consultar(int pos) throws PosicioForaRang;
    public int buscar(E e) throws ElementNoTrobat;
    public boolean existeix(E e);
    public boolean esBuida();
    public int numElements();
    public Iterator<E> iterator();
}
