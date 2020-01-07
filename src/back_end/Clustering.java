package back_end;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org._3pq.jgrapht.edge.DefaultEdge;
import org._3pq.jgrapht.graph.Subgraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultListenableGraph;
import sun.security.provider.certpath.Vertex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

public class Clustering{

    private Graph grapheClustering;
    public Clustering(Graph g, TreeSet<Centrality>userCentraux, HashMap<String, HashMap<String,Integer>>baseLink){
        TreeSet<String> noeudsFiltre= new TreeSet<>();
        for(Centrality c:userCentraux){

            String utilisateurCentral=c.getNom();
            HashMap<String,Integer>baseRetweeter=baseLink.get(utilisateurCentral);
            for(Map.Entry<String,Integer> entry : baseRetweeter.entrySet()) {
                //On prends 10% des noeuds aléatoirement (afin de ne pas surcharger le graphe)
                if(Math.random()>0.999)
                    noeudsFiltre.add( entry.getKey());
            }
        }
        grapheClustering=new AsSubgraph(g, noeudsFiltre);
        //comptage du nombre de noeuds dans la nouveau graphe
        int cmp=0;
        for(Object v:grapheClustering.vertexSet()){
            cmp++;
        }
        System.out.println("Le nouveau graphe contient "+cmp+" noeuds");

        afficherGraphe(grapheClustering);



    }

    public void afficherGraphe(Graph g) {
        JGraphXAdapter<Vertex, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);
        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("C:\\Users\\cedri\\Desktop\\graph.png");
        try
        {
            ImageIO.write(image, "PNG", imgFile);
            System.out.println("Image created successfully!");
            Desktop.getDesktop().open(imgFile);
        } catch (IOException e)
        {
            System.out.println(e.toString());
        }

    }

    public Clustering(){
        System.out.println("Passage construc cent");
    }
}
