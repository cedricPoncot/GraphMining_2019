package back_end;

import it.unimi.dsi.fastutil.Hash;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    //ATTRIBUTS
    private String username; //nom d'utilisateur
    private int nbTweets; //nombre de tweets qu'il a tweeté
    private int nbRetweets;//nombre de fois où il a été retweeté
    private HashMap<String, Integer> retweeters = new HashMap<>(); //personnes qui ont retweeté le user actuel avec le nombre de fois

    //CONSTRUCTEUR
    public User(String username, int nbTweets){
        this.username = username;
        this.nbTweets = nbTweets;
    }

    //GETTERS AND SETTERS
    public int getNbTweets() {
        return nbTweets;
    }

    public int getNbRetweets() {
        return nbRetweets;
    }

    public String getUsername() {
        return username;
    }

    public HashMap<String, Integer> getRetweeters() {
        return retweeters;
    }

    public void setNbTweets(int nbTweets) {
        this.nbTweets = nbTweets;
    }

    public void setNbRetweets(int nbRetweets) {
        this.nbRetweets = nbRetweets;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //METHODES
    public void ajouterRetweeter(String retweeter){
        if(retweeters.containsKey(retweeter)) {
            Integer i = retweeters.get(retweeter);
            i++;
        }
        else {
            retweeters.put(retweeter,1);
        }
    }
}
