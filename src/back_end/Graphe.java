package back_end;

import java.io.FileNotFoundException;

public class Graphe {
    public BaseDeTweet bd;

    public Graphe(String path) throws FileNotFoundException {
        System.out.println("Entrée Import");
        //Création de la base de tweet
        String cheminCSV = path;
        bd = new BaseDeTweet(cheminCSV);
    }

}



