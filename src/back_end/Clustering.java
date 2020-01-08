package back_end;

import com.mxgraph.layout.*;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;
import org._3pq.jgrapht.edge.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import sun.security.provider.certpath.Vertex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Clustering{

    private HashMap<String,Object> vertexGraphAdapter=new HashMap<>();

    //Couleur des communautées
    private final String[] couleurs={"FFFEB5","#CAF7C9","#FFC58B","#8BDFFF","#A58BFF","#FFB5B9","#B5FFEF"};
    private final int cstCouleur=7;

    public Clustering(Graph g, TreeSet<Centrality>userCentraux, HashMap<String, HashMap<String,Integer>>baseLink,int nbPercentage){
        JGraphXAdapter<Vertex, DefaultEdge> graphAdapter =constructionGraph(userCentraux,baseLink,nbPercentage);
        graphToImage(graphAdapter);
    }

    public JGraphXAdapter<Vertex, DefaultEdge> constructionGraph( TreeSet<Centrality>userCentraux, HashMap<String, HashMap<String,Integer>>baseLink,int nbPercentage){
        System.out.println(nbPercentage);
        JGraphXAdapter<Vertex, DefaultEdge> graphAdapter = new JGraphXAdapter(new DirectedWeightedMultigraph(org.jgrapht.graph.DefaultEdge.class));
        int cmp=0;
        //Parcours des différents utilisateurs centraux
        for(Centrality c:userCentraux){
            String utilisateurCentral=c.getNom();
            Object v1;
            if(cmp<cstCouleur)v1=graphAdapter.insertVertex(graphAdapter.getDefaultParent(),null,utilisateurCentral,0,0,20,20,"fillColor="+couleurs[cmp]);
            else v1=graphAdapter.insertVertex(graphAdapter.getDefaultParent(),null,utilisateurCentral,0,0,20,20);
            vertexGraphAdapter.put(utilisateurCentral,v1);
            HashMap<String,Integer>baseRetweeter=baseLink.get(utilisateurCentral);

            for(Map.Entry<String,Integer> entry : baseRetweeter.entrySet()) {
                //On prends un pourcentage des noeuds aléatoirement (afin de ne pas surcharger le graphe)
                if(Math.random()>1-(nbPercentage/100.0)) {
                    Object v2;
                    if(cmp<cstCouleur)v2=graphAdapter.insertVertex(graphAdapter.getDefaultParent(),null,entry.getKey(),0,0,20,20,"fillColor="+couleurs[cmp]);
                    else v2=graphAdapter.insertVertex(graphAdapter.getDefaultParent(),null,entry.getKey(),0,0,20,20);
                    vertexGraphAdapter.put(entry.getKey(),v2);
                    graphAdapter.insertEdge(graphAdapter.getDefaultParent(),null,""+entry.getValue(),v2, v1);
                }
            }
            cmp++;
            if(cmp==cstCouleur)cmp=0;
        }
        //Ajout des arrêtes entre retweeter
        System.out.println("passage arêtes supplémentaires");
        for(Map.Entry<String,Object> entry : vertexGraphAdapter.entrySet()){
            String nameRetweeter=entry.getKey();
            HashMap<String, Integer>baseRetweeter2 = baseLink.get(nameRetweeter);
            if(baseRetweeter2!=null) {
                for (Map.Entry<String, Integer> entry2 : baseRetweeter2.entrySet()) {
                    if (!entry2.getKey().equals(nameRetweeter)&&vertexGraphAdapter.get(entry2.getKey())!=null) {
                        graphAdapter.insertEdge(graphAdapter.getDefaultParent(),null,""+entry2.getValue(),vertexGraphAdapter.get(entry2.getKey()), vertexGraphAdapter.get(nameRetweeter));

                    }
                }
            }
        }
        return graphAdapter;
    }

    public void setStyle(JGraphXAdapter graphAdapter){
        Map<String, Object> edgeStyle = new HashMap<String, Object>();

        mxStylesheet stylesheet = new mxStylesheet();
        Map<String, Object> vertexStyle = new HashMap<String, Object>();
        vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_PERIMETER, mxPerimeter.EllipsePerimeter);
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#CAF7C9");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        stylesheet.setDefaultVertexStyle(vertexStyle);
        graphAdapter.setStylesheet(stylesheet);
    }


    //Transformation du JGraphXAdapter en image et ouverture de l'image
    public void graphToImage(JGraphXAdapter graphAdapter) {
        mxIGraphLayout layout = new  mxFastOrganicLayout(graphAdapter);
        //On change le style des arêtes et vertex
        setStyle(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("graph.png");
        try
        {
            ImageIO.write(image, "PNG", imgFile);
            System.out.println("Image created successfully!");
        } catch (IOException e)
        {
            System.out.println(e.toString());
        }

    }
}
