package frontEnd;

import back_end.Centrality;
import back_end.Graphe;
import back_end.Tweet;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org._3pq.jgrapht.edge.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
//import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.TreeSet;

public class Controller {
    @FXML
    JFXRadioButton rdClimat, rdFoot;

    @FXML
    TableView table, tableAffichage;

    @FXML
    TableColumn col1, col2, col3, col4, col5, col6, col7;

    @FXML
    JFXButton bVisualiser, bCalculer;

    @FXML
    Label lbOrdre, lbVolume, lbDiametre, lbDegreMoy;

    @FXML
    AnchorPane pnGraphe, pnAffichage, pnCalculs;

    private byte dataset = 0; //1: climat 2 : foot
    private Graphe g=null;

    /***********************************FONCTIONS***********************************/

    //Import des données
    public void importerDonnees(){
        if(dataset==1){
            try {
                g = new Graphe("src/data/climat.txt");
            } catch (FileNotFoundException e) {
                errorDialog("Fichier de données non existant !", "Le fichier de données n'a pas été retrouvé.");
            }
            informationDialog("Données importées !", "Vous pouvez à présent afficher les données, afficher les statistiques ou faire du Clustering.");
        }
        else{
            if(dataset==2){
                try {
                    g = new Graphe("src/data/foot.txt");
                } catch (FileNotFoundException e) {
                    errorDialog("Fichier de données non existant !", "Le fichier de données n'a pas été retrouvé.");
                }
                informationDialog("Données importées !", "Vous pouvez à présent afficher les données, afficher les statistiques ou faire du Clustering.");
            }
            else
                errorDialog("Dataset non sélectionné !", "Veuillez sélectionner un dataset à importer.");
        }
    }

    //Calculs : volume, diamètre, ordre etc.
    public void calculs(){
        if(g!=null) {
            DecimalFormat df = new DecimalFormat("0.00");
            lbOrdre.setText(String.valueOf(g.bd.getOrdre()));
            lbDegreMoy.setText(String.valueOf(df.format(g.bd.getDegreeMoyen())));
            lbVolume.setText(String.valueOf(g.bd.getVolume()));
            if (g.bd.getDiametre() == Double.POSITIVE_INFINITY) lbDiametre.setText("+∞");
            else lbDiametre.setText(String.valueOf(g.bd.getDiametre()));
            setTab(g.bd.UserCentraux());
        }
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    //Slection du ejeu de données
    public void selectedDataset(){
        if(rdClimat.isSelected())
            dataset = 1;
        else
            dataset = 2;
    }

    //Affichage des users centraux
    public void setTab(TreeSet<Centrality> tab) {
        ObservableList<Centrality> usersCentraux = FXCollections.observableArrayList(tab);
        table.setItems(usersCentraux);
    }

    //Methodes d'affichages des panes
    public void affichagePane(){
        pnCalculs.setVisible(false);
       // pnGraphe.setVisible(false);
        pnAffichage.setVisible(true);
        if(g!=null)
            afficherTweets();
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    public void calculsPane(){
        pnAffichage.setVisible(false);
        //pnGraphe.setVisible(false);
        pnCalculs.setVisible(true);
        if(g!=null)
            calculs();
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    //Affichage du graphe
    public void graphePane(){
       // pnAffichage.setVisible(false);
       // pnCalculs.setVisible(false);
       // pnGraphe.setVisible(true);
        if(g!=null) {
            System.out.println("création adapter");
            JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g.bd.listenableG);
            System.out.println("création image");
            //mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
            //layout.execute(graphAdapter.getDefaultParent());
            BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
            try {
                System.out.println("écriture image");
                ImageIO.write(image, "PNG", new File("@../../Images/graph.png")); //je sais plus s'il y a le "@" au début ou pas
                System.out.println("Image générée");
            }
            catch(Exception e){
                System.out.println("Problème lors de l'écriture");
            }
        }
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    public void afficherTweets(){
        ObservableList<Tweet> liste = FXCollections.observableArrayList(g.bd.getTweets());
        tableAffichage.setItems(liste);
    }

    /***********************************BOITES DE DIALOGUE***********************************/
    public void informationDialog(String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void errorDialog(String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERREUR");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /***********************************FONCTIONS FXML***********************************/
    @FXML
    public void initialize() {
        col1.setCellValueFactory(new PropertyValueFactory<Centrality, String>("nom"));
        col2.setCellValueFactory(new PropertyValueFactory<Centrality, String>("poids"));

        col3.setCellValueFactory(new PropertyValueFactory<Tweet, String>("identifiant"));
        col4.setCellValueFactory(new PropertyValueFactory<Tweet, String>("tweeter"));
        col5.setCellValueFactory(new PropertyValueFactory<Tweet, String>("date"));
        col6.setCellValueFactory(new PropertyValueFactory<Tweet, String>("texte"));
        col7.setCellValueFactory(new PropertyValueFactory<Tweet, String>("retweeter"));
    }
}
