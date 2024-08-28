package cat.urv.deim;

import cat.urv.deim.exceptions.VertexNoTrobat;
import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;

import java.util.Iterator;


public class Graf<K extends Comparable<K>, V extends Comparable <V>, E> implements IGraf<K,V,E>{

    //Graph attributes
    public HashMapIndirecte<K,NodeVertex<K,V,Edge<K,E>>> vertices;
    public int numEdges;

    public Graf(int size) throws IllegalArgumentException{
        if (size <= 0){
            throw new IllegalArgumentException();
        } else{
            this.vertices = new HashMapIndirecte<K,NodeVertex<K,V,Edge<K,E>>>(size);
        }
    }


    // Vertex operations

    //Add new vertex to the graph. K identifier and V value
    public void inserirVertex(K key, V value){
        NodeVertex<K,V,Edge<K,E>> newVertex = new NodeVertex<K,V,Edge<K,E>>(key, value);
        vertices.inserir(key, newVertex);
    }

    // get value of a vertex given its ID
    public V consultarVertex(K key) throws VertexNoTrobat{
        try {
            return this.vertices.consultar(key).getInfo();
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // remove a vertex, if exists
    public void esborrarVertex(K key) throws VertexNoTrobat{
        try {
            this.vertices.esborrar(key);
            Edge<K,E> removeEdge = new Edge<K,E>(key, null);
            ILlistaGenerica<K> keyList = this.obtenirVertexIDs();
            Iterator<K> keyExplorer = keyList.iterator();

            while(keyExplorer.hasNext()){
                K listKey = keyExplorer.next();
                if(listKey.compareTo(key) < 0){
                    //remove edge from adjacency list
                    try {
                        this.vertices.consultar(listKey).higherAdj.esborrar(removeEdge);
                    } catch (ElementNoTrobat e) {
                        //Nothing happens
                    }

                }
            }
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // check if empty graph
    public boolean esBuida(){
        return this.vertices.esBuida();
    }

    // number of vertexs in the graph
    public int numVertex(){
        return this.vertices.numElements();
    }

    // Obtain all the vertex IDs
    public ILlistaGenerica<K> obtenirVertexIDs(){
        return this.vertices.obtenirClaus();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    // edge operations

    //functions that returns whole vertex class, list of higher adjacencies included
    public NodeVertex<K,V,Edge<K,E>> searchVertex(K key) throws VertexNoTrobat{
        try {
            return this.vertices.consultar(key);
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    public void inserirAresta(K v1, K v2 , E pes) throws VertexNoTrobat{
        /*Edge insertion is ordered (considering undirected graphs), so the vertex with lower value will have the adjacency added */
        try {
            if(v1.compareTo(v2) <= 0){
                //v1 is lower or equal to v2, adjacency added to v1
                Edge<K,E> newEdge = new Edge<K,E>(v2, pes);
                NodeVertex<K,V,Edge<K,E>> vertex = this.searchVertex(v1);
                vertex.higherAdj.inserir(newEdge);
            }
            else{
                //v1 is higher wrt v2, adjancency added to v2
                Edge<K,E> newEdge = new Edge<K,E>(v1, pes);
                NodeVertex<K,V,Edge<K,E>> vertex = this.searchVertex(v2);
                vertex.higherAdj.inserir(newEdge);
            }
        } catch (VertexNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // add edge with zero weight
    public void inserirAresta(K v1, K v2) throws VertexNoTrobat{
        try {
            inserirAresta(v1, v2, null);
        } catch (VertexNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // check if edge exists given two vertices
    public boolean existeixAresta(K v1, K v2) throws VertexNoTrobat{
        try {
            if(v1.compareTo(v2) <= 0){
                //v1 is lower or equal to v2, search for adjacency in v1
                Edge<K,E> newEdge = new Edge<K,E>(v2, null);
                try {
                    this.searchVertex(v1).higherAdj.buscar(newEdge);
                    return true;
                } catch (ElementNoTrobat e) {
                    return false;
                }
            }
            else{
                //v1 is higher wrt v2, adjancency searched in v2
                Edge<K,E> newEdge = new Edge<K,E>(v1, null);
                try {
                    this.searchVertex(v2).higherAdj.buscar(newEdge);
                    return true;
                } catch (ElementNoTrobat e) {
                    return false;
                }

            }
        } catch (VertexNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // Obtain the weight of an edge given the two vertices that connects
    public E consultarAresta(K v1, K v2) throws VertexNoTrobat, ArestaNoTrobada{
        //Check that both vertices exist
        try {
            this.vertices.consultar(v1);
            this.vertices.consultar(v2);
            if(v1.compareTo(v2) <= 0){
                //v1 is lower or equal to v2, search for adjacency in v1
                Edge<K,E> newEdge = new Edge<K,E>(v2, null);
                try {
                    int pos = this.searchVertex(v1).higherAdj.buscar(newEdge);
                    return this.searchVertex(v1).higherAdj.consultar(pos).getWeight();
                } catch (ElementNoTrobat | PosicioForaRang e) {
                    throw new ArestaNoTrobada();
                }
            }
            else{
                //v2 is lower or equal to v1, search for adjacency in v2
                Edge<K,E> newEdge = new Edge<K,E>(v1, null);
                try {
                    int pos = this.searchVertex(v2).higherAdj.buscar(newEdge);
                    return this.searchVertex(v2).higherAdj.consultar(pos).getWeight();
                } catch (ElementNoTrobat | PosicioForaRang e) {
                    throw new ArestaNoTrobada();
                }
            }
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }


    // Method that removes edge given the connected vertices
    public void esborrarAresta(K v1, K v2) throws VertexNoTrobat, ArestaNoTrobada{
        try {
            this.vertices.consultar(v1);
            this.vertices.consultar(v2);
            this.consultarAresta(v1, v2);
            if(v1.compareTo(v2) <= 0){
                //erase edge in v1 adjacency list
                Edge<K,E> newEdge = new Edge<K,E>(v2, null);
                this.searchVertex(v1).higherAdj.esborrar(newEdge);
            }
            else{
                Edge<K,E> newEdge = new Edge<K,E>(v1, null);
                this.searchVertex(v2).higherAdj.esborrar(newEdge);
            }
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        } catch(ArestaNoTrobada e){
            throw new ArestaNoTrobada();
        }
    }

    // Metode per a comptar quantes arestes te el graf en total
    public int numArestes(){
        //1. Obtain a list of all the keys
        ILlistaGenerica<K> keyList = this.vertices.obtenirClaus();

        //2. Loop over all the vertices and count the number of adjacencies they have and add them up
        Iterator<K> explorer = keyList.iterator();
        numEdges = 0;
        while(explorer.hasNext()){
            try {
                numEdges = numEdges + this.vertices.consultar(explorer.next()).higherAdj.numElem;
            } catch (ElementNoTrobat e) {
                System.out.println("This error should not happen");
            }

        }
        return numEdges;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    // Auxiliary methods to work with the graph

    // Method to know if a vertex has neighbours
    public boolean vertexAillat(K v1) throws VertexNoTrobat{
        try {
            if(this.vertices.consultar(v1).higherAdj.numElem >0){
                return false;
            } else{
                ILlistaGenerica<K> keyList = this.vertices.obtenirClaus();//this list is ordered by key

                //2. Loop over all the vertices and count the number of adjacencies they have and add them up
                Iterator<K> explorer = keyList.iterator();
                Edge<K,E> edge = new Edge<K,E>(v1, null);
                boolean cont = true; //condition to save up iterations in the loop. stands for "continue"
                while (explorer.hasNext() && cont == true) {
                    K key = explorer.next();
                    if(key.compareTo(v1) > 0){
                        //the current key is greather than the one we look for, it has no meaning to continue
                        cont = false;
                    }else{
                        //check if the edge is present in any adjacency list of previous vertices
                        if(this.vertices.consultar(key).higherAdj.existeix(edge)){
                            return false;
                        }
                    }
                }
                //if the loop is ended without any return, it means we have isolated vertex
                return true;
            }
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // Degree of a vertex
    public int numVeins(K v1) throws VertexNoTrobat{
        int numNeighbours = 0;
        try {
            numNeighbours = numNeighbours + this.vertices.consultar(v1).higherAdj.numElem;
            ILlistaGenerica<K> keyList = this.vertices.obtenirClaus();//this list is ordered by key

            //2. Loop over all the vertices and count the number of adjacencies they have and add them up
            Iterator<K> explorer = keyList.iterator();
            Edge<K,E> edge = new Edge<K,E>(v1, null);
            boolean cont = true; //condition to save up iterations in the loop. stands for "continue"
            while (explorer.hasNext() && cont == true) {
                K key = explorer.next();
                if(key.compareTo(v1) > 0){
                    //the current key is greather than the one we look for, it has no meaning to continue
                    cont = false;
                }else{
                    //check if the edge is present in any adjacency list of previous vertices
                    if(this.vertices.consultar(key).higherAdj.existeix(edge)){
                        numNeighbours++;
                    }
                }
            }
            return numNeighbours;
        }
        catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    // Metode per a obtenir tots els ID de vertex veins d'un vertex
    public ILlistaGenerica<K> obtenirVeins(K v1) throws VertexNoTrobat{
        try {
            ILlistaGenerica<K> keyList = this.vertices.obtenirClaus();
            LlistaNoOrdenada<K> Neighbours = new LlistaNoOrdenada<K>();
            Iterator<K> explorer = keyList.iterator();
            this.vertices.consultar(v1);
            Iterator<Edge<K,E>> adjExplorer = this.vertices.consultar(v1).higherAdj.iterator();
            while(adjExplorer.hasNext()){
                Neighbours.inserir(adjExplorer.next().getVertexKey());
            }

            Edge<K,E> edge = new Edge<K,E>(v1, null);
            boolean cont = true;
            while(explorer.hasNext() && cont == true){
                K key = explorer.next();
                if(key.compareTo(v1) > 0){
                    //the current key is greather than the one we look for, it has no meaning to continue
                    cont = false;
                }else{
                    //check if the edge is present in any adjacency list of previous vertices
                    if(this.vertices.consultar(key).higherAdj.existeix(edge)){
                        Neighbours.inserir(key);
                    }
                }
            }
            return Neighbours;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }

    }

    //obtain connected component associated to a given vertex ID
    public ILlistaGenerica<K> obtenirNodesConnectats(K v1) throws VertexNoTrobat{
        try {
            this.vertices.buscar(v1);
            //BFS algorithm to be implemented
            LlistaModificada<K> queue = new LlistaModificada<K>();
            HashMapIndirecte<K,K> connected = new HashMapIndirecte<K,K>(numVertex());
            ILlistaGenerica<K> neighbours;
            Node<K> head;
            K keyHead;
            //add the first element to the queue
            queue.inserir(v1);
            connected.inserir(v1, v1);
            while(!queue.esBuida()){
                head = queue.ghost.next;
                keyHead = head.info;

                //add neighbours to queue and connected
                neighbours = this.obtenirVeins(keyHead);
                Iterator<K> explNeighbour = neighbours.iterator();
                while(explNeighbour.hasNext()){
                    //add elements to the queue and to connected
                    K newKey = explNeighbour.next();
                    if(!connected.buscar(newKey)){
                        //to be yet added
                        connected.inserir(newKey, newKey);
                        queue.inserir(newKey);
                    }
                }
                //remove head
                queue.esborrar(keyHead);
            }
            return connected.obtenirClaus();
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }

    }

    // obtain list of vertex IDs of the biggest connected component
    public ILlistaGenerica<K> obtenirComponentConnexaMesGran(){
        Iterator<NodeVertex<K,V,Edge<K,E>>> explorer = this.vertices.iterator();
        ILlistaGenerica<K> connectedComp1;
        ILlistaGenerica<K> connectedComp2;
        K key;
        //first iteration must be outside the loop
        try {
            key = explorer.next().getKey();
        connectedComp1 = this.obtenirNodesConnectats(key);
        while(explorer.hasNext()){
            key = explorer.next().getKey();
            if(!connectedComp1.existeix(key)){
                connectedComp2 = this.obtenirNodesConnectats(key);
                if(connectedComp1.numElements() < connectedComp2.numElements()){
                    connectedComp1 = connectedComp2;
                }
            }
        }

        return connectedComp1;
        } catch (VertexNoTrobat e) {
            return new LlistaOrdenada<K>();
        }
    }
}
