package back_end;



public class Centrality implements Comparable{
    /* Un objet Centrality est composé du nom du tweeter et du nombre de fois qu'il a été retweeté (attribut poids).
     * Plus le poids est grand, plus l'utilisateur est central. */

    /***************************************************ATTRIBUTS****************************************************/
    private String nom;
    private int poids;

    /*************************************************CONSTRUCTEUR**************************************************/
    public Centrality(String nom, int poids){
        this.poids=poids;
        this.nom=nom;
    }

    /***************************************************GETTERS****************************************************/
    public String getNom(){
        return nom;
    }
    public  int getPoids(){
        return poids;
    }

    /**************************************************FONCTIONS**************************************************/
    //Implémentation de compareTo, nécessaire afin de pouvoir comparer 2 objets "Centrality" par rapport à leur poids
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

    //Affichage de l'objet
    @Override
    public String toString() {
        return nom+" "+poids;
    }
};
