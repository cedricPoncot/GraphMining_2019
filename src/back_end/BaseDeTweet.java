package back_end;

import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.ClusteringCoefficient;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.io.*;
import java.util.*;
import java.util.List;


public class BaseDeTweet {
    /* La classe BaseDeTweet contient la liste des tweets importés, ainsi que le graphe construit à partir de ces données.
     * Il y a aussi la structure de données baseLink qui permet de sauvegarder le lien entre un utilisateur et un autre.
     * Cette classe se charge de faire le calculs des statistiques du graphe : diamètre, ordre, degré moyen et volume.*/

    /**************************************************ATTRIBUTS**************************************************/
    private HashMap <String,HashMap<String,Integer>> baseLink = new HashMap();
    /*Le premier String de la première HashMap correspond au username du tweeter, il est relié à une autre hashmap qui contient
     * le username de la personne qui a retweeté le tweeter, ainsi que le nombre de fois qu'elle l'a fait. */
    private HashMap<String,Integer> centrality;
    private ArrayList<Tweet> tweets = new ArrayList<>(); //liste des tweets importés
    public Graph<String, DefaultEdge> g = new DirectedWeightedMultigraph(DefaultEdge.class); //Graphe
    public DefaultListenableGraph listenableG;
    private double degreeMoyen=0;
    private int ordre=0;
    private double diametre=0;
    private int volume=0;

    /**********************************************GETTERS ET SETTERS**********************************************/
    public HashMap<String,HashMap<String,Integer>> getBaseLink(){
        return baseLink;
    }

    public ArrayList<Tweet> getTweets(){
        return tweets;
    }

    public HashMap<String,Integer> getCentrality(){
        return centrality;
    }

    public  double getDiametre(){
        return diametre;
    }

    public double getDegreeMoyen(){
        return degreeMoyen;
    }

    public int getOrdre(){
        return ordre;
    }

    public int getVolume(){
        return volume;
    }

    public void setTweets(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }


    /*****************************************FONCTIONS*******************************************/

    // Cette fonction calcule la centralité pour chaque utilisateur
    private void calculCentralite(Tweet t){
        if (centrality.get(t.getRetweeter()) == null) {
            centrality.put(t.getRetweeter(), 1);
        } else {
            centrality.put(t.getRetweeter(), centrality.get(t.getRetweeter()) + 1);
        }
    }

    // Cette fonction calcule le diametre du graphe précédemment construit
    private void calculDiametre(){
        //Calcul du diamètre
        double distance;
        boolean sortie=false;
        for(String s1:g.vertexSet()) {
            for(String s2:g.vertexSet()){
                DijkstraShortestPath dijkstra=new DijkstraShortestPath(g);
                distance=dijkstra.getPathWeight(s1,s2);
                if(distance>diametre){
                    diametre=distance;
                }
                //Si la distance est infinie, on peut arrêter les calculs : le diamêtre sera +inf
                if(diametre==Double.POSITIVE_INFINITY){
                    sortie=true;
                    //On sort de la première boucle for
                    break;
                }
            }
            //On sort de la seconde boucle for
            if(sortie)break;
        }
    }

    // Cette fonction ajoute au graphe le tweet donné en paramêtre. Il calcule aussi l'ordre par la même occasion.
    private void constructionGraphe(Tweet t){
        if (g.addVertex(t.getRetweeter())) {
            ordre++;
        }
        if (g.addVertex(t.getTweeter())) {
            ordre++;
        }
    }

    // Construction du graphe et calculs
    public void calculs(){
        int sommeDegre=0;
        System.out.println("Passage Calc"+degreeMoyen);
        centrality=new HashMap();
        for(Tweet t: tweets){
            if(t.getRetweeter()!=null) {
                //CALCUL DE LA CENTRALITE POUR CHAQUE USER
                calculCentralite(t);

                //CONSTRUCTION DU GRAPHE ET CALCUL DE L'ORDRE
                constructionGraphe(t);
                //On ne compte pas les gens qui se retweet eux mêmes
                if (!t.getRetweeter().equals(t.getTweeter())) {
                    if (!g.containsEdge(t.getRetweeter(), t.getTweeter())) {
                        g.addEdge(t.getRetweeter(), t.getTweeter());
                        g.setEdgeWeight(t.getRetweeter(), t.getTweeter(), 1);
                        volume++;
                        //On gagne un degré ext et un degré int à chaque ajout d'arrête
                        sommeDegre += 2;

                    } else {
                        int poids = (int) g.getEdgeWeight(g.getEdge(t.getRetweeter(), t.getTweeter())) + 1;
                        g.setEdgeWeight(t.getRetweeter(), t.getTweeter(), poids);
                    }
                }

                if (baseLink.get(t.getRetweeter()) == null) {
                    HashMap<String, Integer> retweeter = new HashMap();
                    retweeter.put(t.getTweeter(), 1);
                    baseLink.put(t.getRetweeter(), retweeter);

                } else {
                    if (baseLink.get(t.getRetweeter()).get(t.getTweeter()) == null) {
                        HashMap<String, Integer> retweeter = baseLink.get(t.getRetweeter());
                        retweeter.put(t.getTweeter(), 1);
                        baseLink.put(t.getRetweeter(), retweeter);
                    } else {
                        int value = baseLink.get(t.getRetweeter()).get(t.getTweeter()) + 1;
                        baseLink.get(t.getRetweeter()).put(t.getTweeter(), value);
                    }
                }
            }
        }
        if(ordre!=0 && degreeMoyen==0) {
            degreeMoyen = sommeDegre*1.0 / ordre;
            System.out.println(degreeMoyen);
            System.out.println(ordre);
        }
        listenableG=new DefaultListenableGraph(g);

        //Calcul du diamètre
        calculDiametre();
    }

    // Calcul le plus court chemin entre les sommets s1 et s2 dans le graphe g (défini en global).
    // Dijkstra est un bon algo pour ce type de graphe car toutes les arêtes sont valuées strictement positives

    // Récuperation des "nbUserCentraux" (constante définie dans les attributs de la classe)  utilisateurs les plus centraux. (Complexité linéaire)
    public TreeSet<Centrality>  UserCentraux(int nbUserCentraux){
        TreeSet<Centrality> treeSetUserCentraux=new TreeSet();
        int poidsMin=0;
        int taille=0;
        for (String i : centrality.keySet()) {
            if(taille< nbUserCentraux){
                treeSetUserCentraux.add(new Centrality(i, centrality.get(i)));
                poidsMin = treeSetUserCentraux.last().getPoids();
                taille++;
            }
            else{
                if(centrality.get(i)>poidsMin) {
                    treeSetUserCentraux.pollLast();
                    treeSetUserCentraux.add(new Centrality(i, centrality.get(i)));
                    poidsMin = treeSetUserCentraux.last().getPoids();
                }
            }
        }
        /*//Affichage des utilisateurs centraux dans la console (utilisée pour les tests)
        for(Centrality c : treeSetUserCentraux){
            System.out.println(c.getNom()+" "+c.getPoids());
        }*/
        return treeSetUserCentraux;
    }

    @Override
    public String toString() {
        return "";

    }

}


