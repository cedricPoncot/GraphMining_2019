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
    /* Clustering crée un graphe simplifié en communautés du graphe initial. Ces communautés sont créées à partir des
     * utilisateurs centraux, on définit une communauté par un utilisateur central et les utilisateurs l'ayant
     * retweeté.
     * Cette classe construit donc le graphe avec des couleurs différentes pour chaque communauté et sauvegarde le
     * résultat dans une image au format PNG. */

    
    /**************************************************ATTRIBUTS***************************************************/
    //Graph JGraphX (celui qui va être visionné)
    private HashMap<String,Object> vertexGraphAdapter=new HashMap<>();
    //Couleur des communautées
    private final String[] couleurs={"FFFEB5","#CAF7C9","#FFC58B","#8BDFFF","#A58BFF","#FFB5B9","#B5FFEF"};
    //Taille du tableau précédent
    private final int cstCouleur=7;


    /*************************************************CONSTRUCTEUR*************************************************/
    public Clustering(TreeSet<Centrality>userCentraux, HashMap<String, HashMap<String,Integer>>baseLink,int nbPercentage){
        JGraphXAdapter<Vertex, DefaultEdge> graphAdapter =constructionGraph(userCentraux,baseLink,nbPercentage);
        //Génération de l'image et affichage / téléchargement
        graphToImage(graphAdapter);
    }

    //Fonction qui construit le JgraphX noeuds par noeuds et arête par arête. Le style du noeuds est modifié en fonction de l'appartenance à une communauté.
    private JGraphXAdapter<Vertex, DefaultEdge> constructionGraph( TreeSet<Centrality>userCentraux, HashMap<String, HashMap<String,Integer>>baseLink,int nbPercentage){
        System.out.println(nbPercentage);
        //Instanciation du JgraphX
        JGraphXAdapter<Vertex, DefaultEdge> graphAdapter = new JGraphXAdapter(new DirectedWeightedMultigraph(org.jgrapht.graph.DefaultEdge.class));
        int cmp=0;
        //Parcours des différents utilisateurs centraux
        for(Centrality c:userCentraux){
            String utilisateurCentral=c.getNom();
            Object v1;
			//On ajoute le noeuds (Vertex) avec sa couleur choisie dans le tableau global couleur.
            if(cmp<cstCouleur)
                v1=graphAdapter.insertVertex(graphAdapter.getDefaultParent(),null,utilisateurCentral,0,0,20,20,"fillColor="+couleurs[cmp]);
            else
                v1=graphAdapter.insertVertex(graphAdapter.getDefaultParent(),null,utilisateurCentral,0,0,20,20);
            //On enregistre les différents objets Vertex dans une Hashmap afin de pouvoir créer les arêtes ultérieurement.

            vertexGraphAdapter.put(utilisateurCentral,v1);
            HashMap<String,Integer>baseRetweeter=baseLink.get(utilisateurCentral);
            //Parcours de la communauté de l'utilisateur c
            for(Map.Entry<String,Integer> entry : baseRetweeter.entrySet()) {
                //On prends un pourcentage des noeuds aléatoirement (afin de ne pas surcharger le graphe)
                if(Math.random()>1-(nbPercentage/100.0)) {
                    Object v2;
                    if(cmp<cstCouleur)
                        v2=graphAdapter.insertVertex(graphAdapter.getDefaultParent(),null,entry.getKey(),0,0,20,20,"fillColor="+couleurs[cmp]);
                    else
                        v2=graphAdapter.insertVertex(graphAdapter.getDefaultParent(),null,entry.getKey(),0,0,20,20);

                    vertexGraphAdapter.put(entry.getKey(),v2);
                    graphAdapter.insertEdge(graphAdapter.getDefaultParent(),null,""+entry.getValue(),v2, v1);
                }
            }
            cmp++;
            //On fait boucler les couleurs si il y a plus de communautés que de couleurs présente dans le tableau global "couleur"
            if(cmp==cstCouleur)
                cmp=0;
        }
        //Ajout des arrêtes entre retweeter
        for(Map.Entry<String,Object> entry : vertexGraphAdapter.entrySet()){
            String nameRetweeter=entry.getKey();
            HashMap<String, Integer>baseRetweeter2 = baseLink.get(nameRetweeter);
            //Vérification que le retweeteur ait été retweeté.
            if(baseRetweeter2!=null) {
                for (Map.Entry<String, Integer> entry2 : baseRetweeter2.entrySet()) {
                    if (!entry2.getKey().equals(nameRetweeter)&&vertexGraphAdapter.get(entry2.getKey())!=null) {
                        graphAdapter.insertEdge(graphAdapter.getDefaultParent(),null,""+entry2.getValue(),vertexGraphAdapter.get(entry2.getKey()), vertexGraphAdapter.get(nameRetweeter));
                    }
                }
            }
        }
        //On retourne le JGraphX ainsi créé
        return graphAdapter;
    }

    private void setStyle(JGraphXAdapter graphAdapter){
   		 //Modifie les propriétés de style des vertex. Afin d'avoir un rendu plus esthétique.
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


    //Transformation du JGraphXAdapter en image et ouverture de cette image
    private void graphToImage(JGraphXAdapter graphAdapter) {
		
        //Séléction d'un layout permettant un visualisation facile des communautés
        mxIGraphLayout layout = new  mxFastOrganicLayout(graphAdapter);
        //On change le style des arêtes et vertex
        setStyle(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        //Création du png
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        //Téléchargement de cette image dans un fichier appelé graph.png présent dans le répertoire courant.
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
