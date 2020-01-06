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

    //Attributs
    private HashMap<String, List<Tweet>>  baseTweet = new HashMap();
    private HashMap <String,HashMap<String,Integer>> baseLink = new HashMap();
    private HashMap<String,Integer> centrality;

    private ArrayList<Tweet> tweets = new ArrayList<>();
    //Graphe
    public Graph<String, DefaultEdge> g = new DirectedWeightedMultigraph(DefaultEdge.class);
    public   DefaultListenableGraph listenableG;
    private double degreeMoyen=0;
    private int ordre=0;
    private double diametre=0;
    private int volume=0;

    //GETTERS
    public HashMap<String, List<Tweet>> getBaseTweet() {
        return baseTweet;
    }
    public HashMap<String,HashMap<String,Integer>> getBaseLink(){
        return baseLink;
    }

    public ArrayList<Tweet> getTweets(){
        return tweets;
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
/*
    //CONSTRUCTEUR
    public BaseDeTweet(String cheminCSV) throws FileNotFoundException{
        importCSV(cheminCSV);
    }
*/

    //FONCTIONS

    //Cette fonction calcule la centralité pour chaque utilisateur
    public void calculCentralite(Tweet t){
        if (centrality.get(t.getRetweeter()) == null) {
            centrality.put(t.getRetweeter(), 1);
        } else {
            centrality.put(t.getRetweeter(), centrality.get(t.getRetweeter()) + 1);
        }
    }

    //Cette fonction calcule le diametre du graphe précédemment construit
    public void calculDiametre(){
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

    //Cette fonction ajoute au graphe le tweet donné en paramêtre. Il calcule aussi l'ordre par la même occasion.
    public void constructionGraphe(Tweet t){
        if (g.addVertex(t.getRetweeter())) {
            ordre++;
        }
        if (g.addVertex(t.getTweeter())) {
            ordre++;
        }
    }

    //construction du graphe et calculs
    public void calculs(){
        int sommeDegre=0;
        int poids_min_tabCentralite=0;
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
                    } else {
                        int poids = (int) g.getEdgeWeight(g.getEdge(t.getRetweeter(), t.getTweeter())) + 1;
                        g.setEdgeWeight(t.getRetweeter(), t.getTweeter(), poids);
                    }
                    //On gagne un degré ext et un degré int à chaque ajout d'arrête
                    sommeDegre += 2;
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
        if(ordre!=0) {
            degreeMoyen = sommeDegre*1.0 / ordre;
            System.out.println(degreeMoyen);
            System.out.println(ordre);
        }
        listenableG=new DefaultListenableGraph(g);

        //Calcul du diamètre
        calculDiametre();
    }

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
                            int poids=(int)g.getEdgeWeight(g.getEdge(data[4], data[1])) + 1;
                            g.setEdgeWeight(data[4], data[1], poids);
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
            listenableG=new DefaultListenableGraph(g);
            /*
            //Création de la matrice d'adjacence
            matriceAdjacence=new int[ordre][ordre];
            //initialisation de la matrice d'adjacence à 0
            for(int i=0;i<ordre;i++){
                for(int j=0;j<ordre;j++){
                    matriceAdjacence[i][j]=0;
                }
            }
            */

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
            //JGraph jgraph = new JGraph(new JGraphModelAdapter( g ) );
            /*
            System.out.println("début cluster");
            ClusteringCoefficient cluster=new ClusteringCoefficient(g);
            System.out.println("fin cluster");
            Map<String,Double> score_cluster=cluster.getScores();
            System.out.println("passage1");
            for (Map.Entry<String, Double> entry : score_cluster.entrySet()) {
                System.out.println("o");
                if(entry.getValue()<1){
                    System.out.println("ok");
                    g.removeVertex(entry.getKey());
                }
            }
            System.out.println("passage2");
            System.out.println(g);
            */
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


    /***RENDRE LE NOMBRE D'UTILISATEURS CENTRAUX MODIFIABLE***/
    public TreeSet<Centrality>  UserCentraux(int nbUserCentraux){
        //Récuperation des "nbUserCentraux" (constante définie dans les attributs de la classe)  utilisateurs les plus centraux. (Complexité linéaire)
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

        for(Centrality c : treeSetUserCentraux){
            System.out.println(c.getNom()+" "+c.getPoids());
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


