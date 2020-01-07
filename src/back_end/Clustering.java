package back_end;

import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;
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
            noeudsFiltre.add(utilisateurCentral);
            HashMap<String,Integer>baseRetweeter=baseLink.get(utilisateurCentral);
            for(Map.Entry<String,Integer> entry : baseRetweeter.entrySet()) {
                //On prends 10% des noeuds aléatoirement (afin de ne pas surcharger le graphe)
                if(Math.random()>0.99)
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

        //grapheClustering.addEdge("LaniieB","Oct_bre",2);
       // g.addEdge("LaniieB","Oct_bre");
       // g.setEdgeWeight("LaniieB","Oct_bre", 1);
        System.out.println(grapheClustering);
        afficherGraphe(grapheClustering);


    }
    public void setStyle(JGraphXAdapter graphAdapter){
        Map<String, Object> edgeStyle = new HashMap<String, Object>();
//edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ORTHOGONAL);

        edgeStyle.put(mxConstants.STYLE_SHAPE,    mxConstants.SHAPE_CONNECTOR);
        edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        edgeStyle.put(mxConstants.STYLE_FONTSIZE, "0");
        edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff");
        mxStylesheet stylesheet = new mxStylesheet();
        stylesheet.setDefaultEdgeStyle(edgeStyle);
        Map<String, Object> vertexStyle = new HashMap<String, Object>();
        vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
        //style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        //style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#CAF7C9");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        stylesheet.setDefaultVertexStyle(vertexStyle);
        graphAdapter.setStylesheet(stylesheet);
    }



    public void afficherGraphe(Graph g) {

        JGraphXAdapter<Vertex, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);
        mxIGraphLayout layout = new  mxFastOrganicLayout(graphAdapter);

        //On change le style des arêtes et vertex
        setStyle(graphAdapter);
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
