package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import java.util.Iterator;

public interface IHashMap<K, V> extends Iterable<V> {

    public void inserir(K key, V value);
    public V consultar(K key) throws ElementNoTrobat;
    public void esborrar(K key) throws ElementNoTrobat;
    public boolean buscar(K key);
    public boolean esBuida();
    public int numElements();
    public ILlistaGenerica<K> obtenirClaus();
    public float factorCarrega();
    public int midaTaula();
    public Iterator<V> iterator();
}
