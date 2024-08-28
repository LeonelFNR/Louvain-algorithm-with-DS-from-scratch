package cat.urv.deim;

/*
import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;
import cat.urv.deim.exceptions.ArestaNoTrobada;*/
import cat.urv.deim.exceptions.VertexNoTrobat;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class PajekReader {
    private Graf<Integer, Integer, Integer> network;

    public PajekReader(int size, String filename){
        network = this.reader(size, filename);
    }

    public Graf<Integer,Integer,Integer> reader(int size, String filename){
        this.network = new Graf<>(size);
        if(filename != null){
            //reading pajek format file
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
                String line;
                boolean readingVertex = false;
                while((line = reader.readLine()) != null){
                    //read each line in pajek format
                    if(line.startsWith("*Vertices")){
                        //start by reading vertex info
                        readingVertex = true;
                    } else if(line.startsWith("*Edges")){
                        //finished reading vertices, now reading edges
                        readingVertex = false;
                    } else if(readingVertex){
                        //reading vertices
                        String[] parts = line.trim().split("\\s+"); //sparse into blocks of info
                        if(parts.length >= 2){
                            int vertexId = Integer.parseInt(parts[0]);
                            // add node to the graf
                            network.inserirVertex(vertexId, vertexId);
                        }
                    } else {
                        //reading edges
                        String[] parts = line.trim().split("\\s+");
                        int v1 = Integer.parseInt(parts[0]);
                        int v2 = Integer.parseInt(parts[1]);
                        int weight;
                        if(parts.length > 2){
                            weight = Integer.parseInt(parts[2]);
                        } else{
                            weight = 1;
                        }
                            //check that vertices have already been added, add if needed
                        try {
                            this.network.consultarVertex(v1);
                        } catch (VertexNoTrobat e) {
                            network.inserirVertex(v1, v1);
                        }
                        try {
                            this.network.consultarVertex(v2);
                        } catch (VertexNoTrobat e) {
                            network.inserirVertex(v2,v2);
                        }
                        try {
                            if(!network.existeixAresta(v1, v2)){
                                network.inserirAresta(v1, v2, weight);
                            }
                        } catch (VertexNoTrobat e) {
                            System.out.println("Un dels vèrtexs de l'aresta no trobat. En principi no hauria de saltar aquest error pq. ja s'ha comprovat");
                        }
                    }
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("Error llegint arxiu: " + filename);
                e.printStackTrace();
            }
        }
        return network;
    }

    public Graf<Integer,Integer, Integer> obtainGraph(){
        return this.network;
    }
}
