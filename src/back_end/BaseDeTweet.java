package back_end;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import sun.security.provider.certpath.Vertex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static org.jgrapht.GraphTests.isConnected;

public class BaseDeTweet {

    //Attributs
    private HashMap<String, List<Tweet>>  baseTweet = new HashMap();
    private HashMap <String,HashMap<String,Integer>> baseLink = new HashMap();
    private HashMap<String,Integer> centrality = new HashMap();
    private ArrayList<Tweet> tweets = new ArrayList<>();
    //Graphe
    Graph<String, DefaultEdge> g = new DirectedWeightedMultigraph<>(DefaultEdge.class);
    static final int nbUserCentraux = 5;
    private double degreeMoyen=0;
    private int ordre=0;
    private int diametre=0;
    private int volume=0;

    //GETTERS
    public HashMap<String, List<Tweet>> getBaseTweet() {
        return baseTweet;
    }

    public ArrayList<Tweet> getTweets(){
        return tweets;
    }

    public  int getDiametre(){
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


    //CONSTRUCTEUR
    public BaseDeTweet(String cheminCSV){
        try {
            importCSV(cheminCSV);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }


    //FONCTIONS

    //Import des données
    void importCSV(String cheminCSV) throws FileNotFoundException {

        //Buffer pour la lecture du fichier
        BufferedReader br=null;
        String line="";

        int sommeDegre=0;
        int poids_min_tabCentralite=0;
        try {
            br=new BufferedReader(new FileReader(cheminCSV));
            int compteur_ligne=0;
            while((line=br.readLine())!=null){
                String[] data=line.split("\t");
                Tweet t = null;
                if(data.length==5){
                    t = new Tweet(data[0],data[1],data[2],data[3],data[4]);
                    tweets.add(t);

                    //Remplissage de la map centrality contenant pour chaque utilisateur sa centralité
                    if(centrality.get(data[4])==null){
                        centrality.put(data[4],1);
                    }
                    else {
                        centrality.put(data[4],centrality.get(data[4])+1);
                    }
                    if(g.addVertex(data[4])){
                        ordre++;
                    }
                    if(g.addVertex(data[1])){
                        ordre++;
                    }
                    //On ne compte pas les gens qui se retweet eux mêmes
                    if(!data[4].equals(data[1])) {
                        if (!g.containsEdge(data[4], data[1])){
                            g.addEdge(data[4], data[1]);
                            g.setEdgeWeight(data[4], data[1], 1);
                            volume++;
                        }
                        else {
                            g.setEdgeWeight(data[4], data[1], g.getEdgeWeight(g.getEdge(data[4], data[1])) + 1);
                        }
                        //On gagne un degré ext et un degré int à chaque ajout d'arrête
                        sommeDegre+=2;
                    }

                    //arêtes
                    if(baseLink.get(data[4])==null ){
                        HashMap<String, Integer> retweeter = new HashMap();
                        retweeter.put(data[1], 1);
                        baseLink.put(data[4], retweeter);

                    }
                    else{
                        if(baseLink.get(data[4]).get(data[1])==null){
                            HashMap<String, Integer> retweeter = baseLink.get(data[4]);
                            retweeter.put(data[1], 1);
                            baseLink.put(data[4], retweeter);
                        }
                        else{
                            int value = baseLink.get(data[4]).get(data[1]) + 1;
                            baseLink.get(data[4]).put(data[1], value);
                        }

                    }
                }
                if(data.length==4) {
                    t = new Tweet(data[0],data[1],data[2],data[3]);
                    tweets.add(t);
                }
                if(data.length !=4 && data.length !=5){
                    System.out.println("Problème de longueur de data. !!!");
                    break;
                }
                /*
                List<Tweet> listeDeTweet;
                if(baseTweet.get(data[1])==null){
                    listeDeTweet=new ArrayList<Tweet>();
                }
                else{
                    listeDeTweet=baseTweet.get(data[1]);
                }
                listeDeTweet.add(t);
                baseTweet.put(data[1],listeDeTweet);
                */
            }
            if(ordre!=0) {
                degreeMoyen = sommeDegre*1.0 / ordre;
                System.out.println(degreeMoyen);
                System.out.println(ordre);
            }

            //Calcul du diamètre
            if(!isConnected(g)){
                diametre=-1;
            }
            else{

                //TODO dans l'idée, faire marcher ce pseudo code

                /*
                int distance;
                for(Vertex s1:g) {
                    for(Vertex s2:g){
                        DijkstraShortestPath dijkstra=new DijkstraShortestPath(g);
                        distance=(int)(dijkstra.getPathWeigth(s1,s2));
                        if(distance>diametre){
                            diametre=distance;
                        }
                    }
                }*/
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try{
                    br.close();
                }
                catch ( IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /*public void afficherGraphe() throws IOException {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("../../Images/graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }*/

    //Calcul le plus court chemin entre les sommets s1 et s2 dans le graphe g (défini en global).
    //Dijkstra est un bon algo pour ce type de graphe car toutes les arêtes sont valuées strictement positives
    int Dijkstra(Vertex s1,Vertex s2){

    }
    public TreeSet<Centrality>  UserCentraux(){
        //Récuperation des "nbUserCentraux" (constante définie dans les attributs de la classe)  utilisateurs les plus centraux. (Complexité linéaire)
        TreeSet<Centrality> treeSetUserCentraux=new TreeSet();
        int poidsMin=0;
        int taille=0;
        for (String i : centrality.keySet()) {
            if(taille< nbUserCentraux){
                treeSetUserCentraux.add(new Centrality(i, centrality.get(i)));
                poidsMin = treeSetUserCentraux.last().poids;
                taille++;
            }
            else{
                if(centrality.get(i)>poidsMin) {
                    treeSetUserCentraux.pollLast();
                    treeSetUserCentraux.add(new Centrality(i, centrality.get(i)));
                    poidsMin = treeSetUserCentraux.last().poids;
                }
            }
        }

        for(Centrality c : treeSetUserCentraux){
            System.out.println(c.nom+" "+c.poids);
        }
        return treeSetUserCentraux;
    }

    @Override
    public String toString() {
        //Affichage des tweets (texte)
        String data="";
        /*
        for(Map.Entry me : baseTweet.entrySet()){
            System.out.println("");
            ArrayList<Tweet> liste=(ArrayList<Tweet>)me.getValue();
            System.out.println("User"+me.getKey()+"   Nombre de tweet :"+liste.size());
            for(Tweet t:liste){
                System.out.println(t);
            }

        }
        */
        /*
        for(Map.Entry me : baseLink.entrySet()){
            for(Map.Entry me2 : baseLink.get(me.getKey()).entrySet()){
                System.out.println("->"+me.getKey()+" _ "+me2.getKey()+" "+me2.getValue());
            }
        }
        */

        //Affichage des arrêtes
        /*
        int maxRT=0;
        String UtilisateurMaxRT="";
        for(Map.Entry me : baseLink.entrySet()){
            int cmptr=0;
            for(Map.Entry me2 : baseLink.get(me.getKey()).entrySet()){
                cmptr+=(int)me2.getValue();
                System.out.println("->"+me.getKey()+" _ "+me2.getKey()+" "+me2.getValue());
            }
            if(baseLink.get(me.getKey()).size()>maxRT){
                maxRT=cmptr;
                UtilisateurMaxRT=(String)me.getKey();
            }
        }
        System.out.println("\nMax RT="+maxRT+" by "+ UtilisateurMaxRT);
        */
        return "";

    }


}


