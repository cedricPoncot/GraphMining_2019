package back_end;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Graphe {
    public BaseDeTweet bd;
    /***SUPPRIMER CETTE CLASSE***/

    public Graphe(String path, int nbLignes) throws FileNotFoundException {
        System.out.println("Entrée Import");
        //Création de la base de tweet
        bd = new BaseDeTweet();
    }

}



