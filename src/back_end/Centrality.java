package back_end;

public class Centrality implements Comparable{
    public String nom;
    public int poids;

    public Centrality(String nom, int poids){
        this.poids=poids;
        this.nom=nom;
    }

    public int compareTo(Object o){
        Centrality c=(Centrality)o;
        if(this.poids<c.poids){
            return 1;
        }
        if(this.poids==c.poids){
            return this.nom.compareTo(c.nom);
        }
        return  -1;
    }

    @Override
    public String toString() {
        return nom+" "+poids;
    }
};
