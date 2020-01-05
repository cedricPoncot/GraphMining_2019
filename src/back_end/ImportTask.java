package back_end;

import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportTask extends Task<ArrayList> {
    /* ImportTask étend la classe abstraite Task<V>, elle doit alors implémenter la fonction call() de cette dernière.
     * Cette tâche sera executée dans un Thread séparé pour ne pas bloquer le Thread de la GUI (JavaFX Application Thread).
     * Cette tâche retourne uen ArrayList de Tweets créés à partir des données du fichier lu.*/

    //ATTRIBUTS
    private String cheminCsv; //Chemin du fichier à lire
    private int nbTweetsTotal; //Nombre de lignes du fichier (pour afficher la progression de la tâche)

    //CONSTRUCTEUR
    public ImportTask(String cheminCsv, int nbTweetsTotal){
        this.cheminCsv = cheminCsv;
        this.nbTweetsTotal = nbTweetsTotal;
    }

    @Override
    protected ArrayList call() throws Exception {
        BufferedReader br=null; //Buffer pour la lecture du fichier
        String line= ""; //Chaîne contenant la ligne lue
        ArrayList<Tweet> tweets = new ArrayList<>(); //Liste des tweets qui sera remplie avec les tweets du fichier

        try {
            br = new BufferedReader(new FileReader(cheminCsv));
            int nbTweetsLus = 0; //Compte le nombre de tweets déjà lus
            while ((line = br.readLine()) != null) { //Tant que le fichier n'est pas vide
                String[] data = line.split("\t"); //Lire la ligne du fichier
                nbTweetsLus++;
                if(data.length==5)
                    tweets.add(new Tweet(data[0], data[1], data[2], data[3], data[4]));
                else {
                    if (data.length == 4)
                        tweets.add(new Tweet(data[0], data[1], data[2], data[3]));
                }
                this.updateProgress(nbTweetsLus, nbTweetsTotal); //Actualiser le progrès de la tâche
                message(nbTweetsLus); //Actualiser le message à afficher
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally { //Fermer le buffer à la fin de la lecture ou s'il y a une erreur lors de la lecture
            if(br!=null){
                try{
                    br.close();
                }
                catch ( IOException e){
                    e.printStackTrace();
                }
            }
        }
        return tweets; //Retourner une ArrayList des tweets récupérés à partir du fichier lu
    }

    //Message à afficher au cours de l'exécution de la tâche
    private void message(int nbTweetsLus) throws InterruptedException {
        //Affiche le nombre de tweets importés sur le nombre de tweets total
        this.updateMessage(nbTweetsLus+" sur "+nbTweetsTotal+" tweets");
    }

}
