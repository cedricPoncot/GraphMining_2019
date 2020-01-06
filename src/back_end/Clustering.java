package back_end;

import org._3pq.jgrapht.graph.Subgraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Clustering{
    Graph grapheClustering;
    Clustering(Graph g, TreeSet<String>userCentraux, HashMap<String, HashMap<String,Integer>>baseLink){
        TreeSet<String> noeudsFiltre= new TreeSet<>();
        for(String utilisateurCentral:userCentraux){
            HashMap<String,Integer>baseRetweeter=baseLink.get(utilisateurCentral);
            for(Map.Entry<String,Integer> entry : baseRetweeter.entrySet()) {
                noeudsFiltre.add( entry.getKey());
            }
        }
        grapheClustering=new AsSubgraph(g, noeudsFiltre);
    }
}
