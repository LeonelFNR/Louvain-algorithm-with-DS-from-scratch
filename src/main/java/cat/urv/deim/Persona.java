package cat.urv.deim;

public class Persona implements Comparable<Persona>{
    private int id_persona;
    private String nom;
    private String cognom;
    private int edat;
    private int pes;
    private int alsada;

    public Persona(int id_persona, int edat, String nom, String cognom, int alsada, int pes) {
        this.id_persona = id_persona;
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
        this.pes = pes;
        this.alsada = alsada;
    }

    public int getId_persona() {
        return id_persona;
    }

    public String getNom() {
        return nom;
    }

    public String getCognom() {
        return cognom;
    }

    public int getEdat() {
        return edat;
    }

    public int getPes() {
        return pes;
    }

    public int getAlsada() {
        return alsada;
    }

    public int compareTo(Persona p) {
        int idComparison = p.getId_persona() - this.getId_persona();
        if(idComparison < 0){
            //p id smaller
            return 1;
        }
        else if(idComparison == 0){
            //same id
            return 0;
        } else{
            //p id bigger
            return -1;
        }
    }

    public boolean equals(Persona p) {
        return p.getId_persona() == this.getId_persona();
    }
}

