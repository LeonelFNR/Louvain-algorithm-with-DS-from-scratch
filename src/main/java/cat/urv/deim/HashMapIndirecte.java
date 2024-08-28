package cat.urv.deim;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;

import cat.urv.deim.exceptions.ElementNoTrobat;

public class HashMapIndirecte<K extends Comparable<K>, V> implements IHashMap<K,V> {

    private int numElem;
    private int size;
    private ArrayList<NodeTable<K,V>> table;
    private float chargeFactor = 0.75f;

    //builder
    public HashMapIndirecte(int size) throws IllegalArgumentException{
        if(size <= 0){
            throw new IllegalArgumentException();
        }
        this.size = size;
        this.numElem = 0;
        this.table = new ArrayList<NodeTable<K,V>>();

        for (int i = 0; i < size; i++){
            table.add(new NodeTable<K,V>());
        }

    }

    //hash function
    public int hash (K key){
        if (key != null){
            return Math.abs(key.hashCode()) % size;
        } else{
            return 0;
        }
    }
    //Method for adding element ot the table. if it the key already exists, the value is updated
    public void inserir(K key, V value){
        NodeHash<K,V> add = new NodeHash<K,V>(key, value);
        if(factorCarrega() >= chargeFactor){
            resize();
        }
        int pos = hash(key);
        int listNumElem = table.get(pos).nodeList.numElem;
        //the pointer to the list was already created, we just have to add to the list
        table.get(pos).nodeList.inserir(add);
        if(listNumElem < table.get(pos).nodeList.numElem){
            numElem++; //because an element has been actually added, not overwritten
        }

    }

    public void resize(){
        int newSize = 2*size;
        ArrayList<NodeTable<K,V>> reTable = new ArrayList<NodeTable<K,V>>();

        for(int i = 0; i< newSize; i++){
            reTable.add(new NodeTable<K,V>());
        }
        for(NodeTable<K,V> explorer : table){
            Iterator<NodeHash<K,V>> listExplorer = explorer.nodeList.iterator();
            while(listExplorer.hasNext()){
                //using the .next(), we leave the value of the iteration in a variable
                //and automatically move to the next one
                NodeHash<K,V> current = listExplorer.next();
                int newPos = Math.abs(current.getKey().hashCode()) % newSize;
                //add element, taking into account that we want to add the NodeHash, not the Node<NodeHash>, so we'll add .info
                reTable.get(newPos).nodeList.inserir(current);
            }
        }
        //update the main variables
        size = newSize;
        table = reTable;
    }

    // array with all the elements of K
    public V consultar(K key) throws ElementNoTrobat{
        if (key == null){
            throw new ElementNoTrobat();
        } else{
            int pos = hash(key);
            if(table.get(pos) == null){
                throw new ElementNoTrobat();
            } else{
                NodeHash<K,V> query = new NodeHash<K,V>(key, null);
                Node<NodeHash<K,V>> explorer = table.get(pos).nodeList.ghost.next;
                while(explorer != null && explorer.info.compareTo(query) != 0){
                    explorer = explorer.next;
                }
                if(explorer == null){
                    throw new ElementNoTrobat();
                }
                else{
                    return explorer.info.getValue();
                }
            }
        }
    }

    // Erase element from hash table
    public void esborrar(K key) throws ElementNoTrobat{
        if (key == null){
            throw new ElementNoTrobat();
        } else{
            int pos = hash(key);
            if(table.get(pos).nodeList.ghost.next == null){
                throw new ElementNoTrobat();
            } else{
                NodeHash<K,V> query = new NodeHash<K,V>(key, null);
                try {
                    table.get(pos).nodeList.esborrar(query);
                    numElem--;
                } catch (ElementNoTrobat e) {
                    throw new ElementNoTrobat();
                }
            }
        }
    }

    // check if elements in hash table exists
    public boolean buscar(K key){
        if (key == null){
            return false;
        } else{
            int pos = hash(key);
            if(table.get(pos) == null){
                return false;
            } else{
                NodeHash<K,V> query = new NodeHash<K,V>(key, null);
                try {
                    table.get(pos).nodeList.buscar(query);
                    return true;
                } catch (ElementNoTrobat e) {
                    return false;
                }
            }
        }
    }

    // check if empty hash table
    public boolean esBuida(){
        return numElem == 0;
    }

    // number of elements in the table
    public int numElements(){
        return numElem;
    }

    // obtain list of all the elements in the table
    public ILlistaGenerica<K> obtenirClaus(){
        LlistaOrdenada<K> keyList = new LlistaOrdenada<K>();
        for(NodeTable<K,V> explorer : table){
            if(explorer != null){
                Iterator<NodeHash<K,V>> listExplorer = explorer.nodeList.iterator();
                while(listExplorer.hasNext()){
                    keyList.inserir(listExplorer.next().getKey());
                }
            }
        }
        return keyList;
    }

    // charge factor calculator
    public float factorCarrega(){
        float charge = (float) numElem / size;
        return charge;
    }

    // table size
    public int midaTaula(){
        return size;
    }


    public Iterator<V> iterator(){
        return new goThrough();
    }

    private class goThrough implements Iterator<V>{

        private LlistaOrdenada<K> keys;
        private Iterator<K> iteratorList;

        private goThrough(){
            this.keys = new LlistaOrdenada<K>();
            for(NodeTable<K,V> explorer : table){
                if(explorer != null){
                    Iterator<NodeHash<K,V>> listExplorer = explorer.nodeList.iterator();
                    while(listExplorer.hasNext()){
                        //add key to list of keys
                        keys.inserir(listExplorer.next().getKey());
                    }

                }
            }
            this.iteratorList = keys.iterator();
        }

        @Override
        public boolean hasNext(){
            return iteratorList.hasNext();
        }
        @Override
        public V next(){
            V current;
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            try {
                current = consultar(iteratorList.next());
                return current;
            } catch (ElementNoTrobat e) {
                throw new NoSuchElementException();
            }
        }
    }
}
