package cat.urv.deim;

import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.VertexNoTrobat;

public interface IGraf<K, V, E>  {

    ////////////////////////////////////////////////////////////////////////////////////
    // Vertex operations

    public void inserirVertex(K key, V value);
    public V consultarVertex(K key) throws VertexNoTrobat;
    public void esborrarVertex(K key) throws VertexNoTrobat;
    public boolean esBuida();
    public int numVertex();
    public ILlistaGenerica<K> obtenirVertexIDs();

    ////////////////////////////////////////////////////////////////////////////////////
    // Edge operations

    public void inserirAresta(K v1, K v2 , E pes) throws VertexNoTrobat;
    public void inserirAresta(K v1, K v2) throws VertexNoTrobat;
    public boolean existeixAresta(K v1, K v2) throws VertexNoTrobat;
    public E consultarAresta(K v1, K v2) throws VertexNoTrobat, ArestaNoTrobada;
    public void esborrarAresta(K v1, K v2) throws VertexNoTrobat, ArestaNoTrobada;
    public int numArestes();

    ////////////////////////////////////////////////////////////////////////////////////
    // Extra operations
    public boolean vertexAillat(K v1) throws VertexNoTrobat;
    public int numVeins(K v1) throws VertexNoTrobat;
    public ILlistaGenerica<K> obtenirVeins(K v1) throws VertexNoTrobat;
    public ILlistaGenerica<K> obtenirNodesConnectats(K v1) throws VertexNoTrobat;
    public ILlistaGenerica<K> obtenirComponentConnexaMesGran();

}
