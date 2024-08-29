package cat.urv.deim;

import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.VertexNoTrobat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;


//structure and opperations of comunities
public class DSComunity implements Iterable<HashMapIndirecte<Integer,Integer>>{

    private IHashMap<Integer,HashMapIndirecte<Integer,Integer>> comunities;
    private int numComunities;
    private int startingSize = 5; //arbitrary starting size for comuunities
    private int numComStarting = 10; //arbitrary starting size for number of communities table
    private Graf<Integer,Integer,Integer> graph; //graph that we will study

    //builder
    private DSComunity( Graf<Integer,Integer,Integer> graph){
        this.numComunities = 0;
        this.comunities = new HashMapIndirecte<Integer,HashMapIndirecte<Integer,Integer>>(numComStarting);
        this.graph = graph;
    }
    //create new community
    private void newCommunity(Integer comNumber){
        this.comunities.inserir(comNumber, new HashMapIndirecte<>(startingSize));
        numComunities++;
    }
    //add a precreated community
    private void newCommunity(Integer comNumber, HashMapIndirecte<Integer,Integer> community){
        this.comunities.inserir(comNumber, community);
        numComunities++;
    }

    //remove community. IN order to properly erase it, the community must be EMPTY
    private void removeComunity(Integer comunID) throws ElementNoTrobat{
        try {
            if(this.comunities.consultar(comunID).esBuida()){
                this.comunities.esborrar(comunID);
                this.numComunities--;
            }
            else{
                //community cannot be erased
                System.out.println("Community cannot be removed since it has elements in it");
            }
        } catch (ElementNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public int numComunities(){
        return this.numComunities;
    }

    public HashMapIndirecte<Integer,Integer> getComunity(Integer comunID) throws ElementNoTrobat{
        try {
            return this.comunities.consultar(comunID);
        } catch (ElementNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    //assigns a certain vertex to the specified community
    private void vertexToCommunity(Integer vertexID, Integer comunID)throws ElementNoTrobat{
        try {
            this.comunities.consultar(comunID).inserir(vertexID, vertexID);
        } catch (ElementNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    //assign each vertex to a community
    private void communizer(){
        //set initial number of communities to zero
        numComunities = 0;
        this.comunities = new HashMapIndirecte<Integer,HashMapIndirecte<Integer,Integer>>(this.graph.numVertex());
        //creates the initial community distribution given a graph. assigns each vertex to a community
        ILlistaGenerica<Integer> vertexIdList =this.graph.obtenirVertexIDs();
        Iterator<Integer> vertexIterator = vertexIdList.iterator();
        Integer vertexID;
        while (vertexIterator.hasNext()) {
            vertexID = vertexIterator.next();
            this.newCommunity(vertexID);
            try {
                this.vertexToCommunity(vertexID, vertexID); //assign vertex to "own" community
            } catch (ElementNoTrobat e) {
                System.out.println("This error should not happen");
            }
        }
    }

    //remove vertex from a certain community
    private void removeVertex(Integer vertexID, Integer comunID)throws ElementNoTrobat{
        try {
            HashMapIndirecte<Integer,Integer> community = this.comunities.consultar(comunID);
            try {
                community.esborrar(vertexID);
            } catch (ElementNoTrobat e) {
                System.out.println("Vertex not present in the community specified");
            throw new ElementNoTrobat();
            }
        } catch (ElementNoTrobat e) {
            System.out.println("Community not found");
            throw new ElementNoTrobat();
        }
    }

    //combine communities
    private void combineComunities(Integer com1, Integer com2) throws ElementNoTrobat{
        try {
            if(com1.compareTo(com2) != 0){
                //check that both communities exist
                HashMapIndirecte<Integer,Integer> firstCom= this.comunities.consultar(com1);
                HashMapIndirecte<Integer,Integer> secondCom = this.comunities.consultar(com2);

                //FIRST COMMUNITY GOES INTO THE SECOND ONE
                ILlistaGenerica<Integer> vertexList = firstCom.obtenirClaus();
                Iterator<Integer> vertexIterator = vertexList.iterator();
                while(vertexIterator.hasNext()){
                    Integer vertexId = vertexIterator.next();
                    firstCom.esborrar(vertexId);
                    secondCom.inserir(vertexId, vertexId);
                }
                //once the first community is empty, erase it
                this.removeComunity(com1);
            }
        } catch (ElementNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }


    //uncombine communitites: an auxiliar copy of the original community needs to have been previously prepared
    private void uncombineComunities(Integer com1, Integer com2, HashMapIndirecte<Integer,Integer> copy){
        //copy stands for the original community com1 that we want to restore and was merged in community,
        //com1 is the original id of the merged community
        try {
            if(com1.compareTo(com2) != 0){
                //create auxiliar copy in order not to mess up with the references of the hashmaps
                HashMapIndirecte<Integer,Integer> copyOfTheCopy = this.communityCopy(copy);

                HashMapIndirecte<Integer,Integer> community = this.comunities.consultar(com2);
                this.newCommunity(com1, copyOfTheCopy);
                //remove vertices from previous community
                Iterator<Integer> vertexRemover = copyOfTheCopy.iterator();
                while(vertexRemover.hasNext()){
                    community.esborrar(vertexRemover.next());
                }
            }
        } catch (ElementNoTrobat e) {
            System.out.println("this error should not happen");
        }
    }

    //returns the weighted degree of a vertice
    private Integer weightedDegree(int vertexID){
        Integer wdegree = 0;
        ILlistaGenerica<Integer> neighbours;
        Integer edgeWeight;
        try {
            neighbours = this.graph.obtenirVeins(vertexID);
            Iterator<Integer> neighExplorer = neighbours.iterator();
            while(neighExplorer.hasNext()){
                try {
                    edgeWeight = this.graph.consultarAresta(vertexID, neighExplorer.next());
                } catch (ArestaNoTrobada e) {
                    edgeWeight = 0;
                }
                wdegree = wdegree + edgeWeight;
            }


        } catch (VertexNoTrobat e) {
            System.out.println("This error should not happen");
        }

        return wdegree;
    }

    private float modularity(){
        float modularity = 0.0f;
        int m = graph.numArestes();
        float resolution = 1.0f;
        ILlistaGenerica<Integer> comLIst = this.comunities.obtenirClaus();
        Iterator<Integer> comExplorer = comLIst.iterator();
        Integer Lc, kc, comId;

        while(comExplorer.hasNext()){
            comId = comExplorer.next();
            Lc = this.sumLc(comId);
            kc = this.kc(comId);
            modularity = modularity + (float) Lc / (float) m - resolution*((float) kc / (2*(float) m))*((float) kc / (2*(float) m));
        }
        return modularity;
    }

    private Integer sumLc(Integer comId){
        Integer Lc = 0;
        try {

            ILlistaGenerica<Integer> vertexList = this.comunities.consultar(comId).obtenirClaus();
            Iterator<Integer> vExplorer = vertexList.iterator();

            while(vExplorer.hasNext()){
                //check first if there is self adjancency
                Integer v1 = vExplorer.next();
                Iterator<Integer> vExplorer2 = vertexList.iterator();
                while(vExplorer2.hasNext()){
                    Integer v2 = vExplorer2.next();
                    if(v2.compareTo(v1) >= 0){
                        try {
                            Lc = Lc + this.graph.consultarAresta(v1, v2);
                        } catch (VertexNoTrobat | ArestaNoTrobada e) {} //not a relevant error
                    }
                }
            }

        } catch (ElementNoTrobat e) {
            System.out.println("This error should not be happening.");
        }
        return Lc;
    }

    private Integer kc(Integer comId){
        Integer kc = 0;
        try {
            ILlistaGenerica<Integer> vertexList = this.comunities.consultar(comId).obtenirClaus();
            Iterator<Integer> vExplorer = vertexList.iterator();
            while(vExplorer.hasNext()){
                Integer v1 = vExplorer.next();
                kc = kc + this.weightedDegree(v1);
            }
        } catch (ElementNoTrobat e) {
            System.out.println("This error should not be happening.");
        }
        return kc;
    }

    //auxiliar function of the louvain algorithm that coppies a community
    private HashMapIndirecte<Integer,Integer> communityCopy(HashMapIndirecte<Integer,Integer> com){
        HashMapIndirecte<Integer,Integer> copy = new HashMapIndirecte<Integer,Integer>(com.midaTaula());
        Iterator<Integer> comExplorer = com.iterator();
        Integer key; //key and value here will be the same, so no distinction is needed

        while(comExplorer.hasNext()){
            key = comExplorer.next();
            copy.inserir(key,key);
        }
        return copy;
    }

    private void louvain(){
        //1. assign each vertex to its own community and save the current modularity
        this.communizer();
        float bestModu = this.modularity();
        float louvainModu = bestModu; //initial modularity, with the same value assigned
        float currentModu;
        boolean isImproving = true;
        HashMapIndirecte<Integer,Integer> currentCom;
        HashMapIndirecte<Integer,Integer> copy;
        Integer com1, com2; //community id's
        ILlistaGenerica<Integer> comIdList;
        Iterator<Integer> comIdExplorer1, comIdExplorer2;
        Integer bestCom; //best community to merge with

        try {
            //louvain algorithm. it is applied while modularity improves
            while (isImproving) {
                //go over all comunities
                comIdList = this.comunities.obtenirClaus();
                comIdExplorer1 = comIdList.iterator();
                while(comIdExplorer1.hasNext()){
                    com1 = comIdExplorer1.next();
                    currentCom = this.comunities.consultar(com1);
                    bestCom = com1; //to initialize it at some value
                    //copy the community of study to keep trak
                    copy = this.communityCopy(currentCom);
                    //now, we will fuse the community with others to see which config improves modularity the most
                    //id list may change in these loops, here we need to update it
                    comIdList = this.comunities.obtenirClaus();
                    comIdExplorer2 = comIdList.iterator();
                    while(comIdExplorer2.hasNext()){
                        com2 = comIdExplorer2.next();
                        this.combineComunities(com1, com2);
                        currentModu = this.modularity();
                        //check if merging improves modularity
                        if(currentModu > louvainModu){
                            //improves!, save the "best" id and update louvainModu
                            bestCom = com2;
                            louvainModu = this.modularity();
                        }
                        //undo the change at the moment, in order to compare others
                        this.uncombineComunities(com1, com2, copy);
                    }
                    this.combineComunities(com1, bestCom);
                }
                currentModu = this.modularity();
                if(louvainModu > bestModu){
                    //it has improved
                    bestModu = louvainModu;
                } else{
                    //not improved ==> stop
                    isImproving = false;
                }
            }
        } catch (ElementNoTrobat e) {
            System.out.println("Error should not happen.");
        }
    }

    //move vertex from Community 1 to Community 2. errors wont happen by construction when calling this method
    private void moveTo(Integer vertexId, Integer com1, Integer com2){
        try {
            if(com1.compareTo(com2) != 0){
                HashMapIndirecte<Integer,Integer> Community1, Community2;
                Community1 = this.comunities.consultar(com1);
                Community2 = this.comunities.consultar(com2);
                Community1.esborrar(vertexId);
                Community2.inserir(vertexId, vertexId);
            }
        } catch (ElementNoTrobat e) {
            System.out.println("This error should not happen");
        }
    }

    //refiner. apply after louvain
    private void refiner(){
        ILlistaGenerica<Integer> comList = this.comunities.obtenirClaus();
        Iterator<Integer> comIterator = comList.iterator();
        Iterator<Integer> secComIterator;
        Integer com1, com2, bestCom;
        Iterator<Integer> vIterator;
        Integer vId;
        float currentMod = this.modularity();
        float newMod = currentMod; //to have it initialized.

        while(comIterator.hasNext()){
            com1 = comIterator.next();
            try {
                vIterator = this.comunities.consultar(com1).obtenirClaus().iterator();
                while(vIterator.hasNext()){
                    vId = vIterator.next();
                    bestCom = com1;
                    //now we generate a second iterator of communitites
                    secComIterator = this.comunities.obtenirClaus().iterator();
                    while(secComIterator.hasNext()){
                        com2 = secComIterator.next();
                        //change vertex from com1 to com 2
                        this.moveTo(vId, com1, com2);
                        //compute modularity change
                        newMod = this.modularity();
                        if(newMod > currentMod){
                            bestCom = com2;
                            currentMod = newMod;
                        }
                        //undo the process so that we can looking for the best community change
                        this.moveTo(vId, com2, com1);
                    }
                    //move the vertex to the best community
                    this.moveTo(vId, com1, bestCom);
                }
            } catch (ElementNoTrobat e) {}//wont happen
        }
    }

    public Iterator<HashMapIndirecte<Integer,Integer>> iterator(){
        return this.comunities.iterator();
    }

    public Graf<Integer,Integer,Integer> getGraph(){
        return this.graph;
    }

    //creates file with community distribution
    public void creatComunityFile(String filename){
        this.louvain();
        this.refiner();
        float finalModu = this.modularity();
        int keyValue, vertexValue; //id community, id vertex
        HashMapIndirecte<Integer,Integer> comunity;
        Iterator<Integer> comIterator;

        HashMapIndirecte<Integer,Integer> comunityDistribution = new HashMapIndirecte<>(graph.numVertex());

        //create ordered list of all the vertices and their respective community
        ILlistaGenerica<Integer> keyComunities = this.comunities.obtenirClaus();
        Iterator<Integer> keycomExplorer = keyComunities.iterator();
        try {
            while(keycomExplorer.hasNext()){
                keyValue = keycomExplorer.next();
                comunity = this.comunities.consultar(keyValue);
                comIterator = comunity.iterator();
                while (comIterator.hasNext()) {
                    vertexValue = comIterator.next();
                    comunityDistribution.inserir(vertexValue, keyValue);
                }
            }
            ILlistaGenerica<Integer> vertexList = comunityDistribution.obtenirClaus();
            Iterator<Integer> vListIterator = vertexList.iterator();
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename + "_" + finalModu + ".clu"))) {
                writer.write("*Vertices " + String.valueOf(this.graph.numVertex()));
                writer.newLine();
                while(vListIterator.hasNext()){
                    writer.write(String.valueOf(comunityDistribution.consultar(vListIterator.next())));
                    writer.newLine();
                }
            }catch (IOException e) {
                    System.out.println("Error creating file.");
            }
        }  catch (ElementNoTrobat e) {
            System.out.println("This error should not happen.");
        }
    }

}
