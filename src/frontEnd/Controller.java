package frontEnd;

import back_end.Centrality;
import back_end.Graphe;
import back_end.ImportTask;
import back_end.Tweet;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.mxgraph.util.mxCellRenderer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;

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
    JFXButton bVisualiser, bCalculer, bImport, bAnnuler;

    @FXML
    Label lbOrdre, lbVolume, lbDiametre, lbDegreMoy, lbProgressStatus;

    @FXML
    AnchorPane pnGraphe, pnAffichage, pnCalculs, pnImport;

    @FXML
    JFXProgressBar importProgress;

    @FXML
    HBox menuBox;

    private byte dataset = 0; //1: climat 2 : foot
    private Graphe g=null;

    /***********************************FONCTIONS***********************************/

    //Import des données
    public void importerDonnees() {
        bImport.setDisable(true);
        bAnnuler.setDisable(false);
        menuBox.setDisable(true);
        String path = "";
        int nbLignes=0;

        //selection du dataset
        if(dataset==1){
            path = "src/data/climat.txt";
            nbLignes = 1977769;
        }
        else{
            if(dataset==2){
                path ="src/data/foot.txt";
                nbLignes= 899597;
            }
            else
                errorDialog("Dataset non sélectionné !", "Veuillez sélectionner un dataset à importer.");
        }

        //Création du graphe et import des données
        try {
            g = new Graphe(path, nbLignes);

            //Import des données
            importProgress.setVisible(true);
            importProgress.setProgress(0);

            ImportTask importTask = new ImportTask(path, nbLignes); // création de la tâche d'import des données

            importProgress.progressProperty().unbind();
            importProgress.progressProperty().bind(importTask.progressProperty());
            lbProgressStatus.textProperty().unbind();
            lbProgressStatus.textProperty().bind(importTask.messageProperty());

            new Thread(importTask).start(); // création et lancement du thread d'import des données

            //Lorsque la tâche d'import est terminée :
            importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
                if(importTask.isDone()) { //quand l'import est terminé : on récupère le résultat
                    g.bd.setTweets(importTask.getValue());
                    importProgress.progressProperty().unbind();
                    importProgress.setProgress(0);
                    importProgress.setVisible(false);
                    lbProgressStatus.textProperty().unbind();
                    lbProgressStatus.setText("Chargement des données terminé.");
                    bImport.setDisable(false);
                    menuBox.setDisable(false);
                    bAnnuler.setDisable(true);
                    informationDialog("Données importées !", "Vous pouvez à présent afficher les données, afficher les statistiques ou faire du Clustering.");
                }
            });

            //Annulation de l'import
            bAnnuler.setOnAction(event -> {
                bImport.setDisable(false);
                bAnnuler.setDisable(true);
                menuBox.setDisable(false);
                importTask.cancel(true);
                importProgress.progressProperty().unbind();
                lbProgressStatus.textProperty().unbind();
                lbProgressStatus.setText("Chargement des données annulé");
                importProgress.setProgress(0);
            });

        } catch (FileNotFoundException e) {
            errorDialog("Fichier de données non existant !", "Le fichier de données n'a pas été retrouvé.");
        }

    }


    //Calculs : volume, diamètre, ordre etc.
    public void calculs(){
        if(g!=null) {
            Task statsTask = new Task() { //Création de la tache de calcul
                @Override
                protected Object call() {
                    g.bd.calculs();
                    return null;
                }
            };

            new Thread(statsTask).start(); //Lancer le thread des calculs

            informationDialog("Caluls des statistiques en cours", "Les calculs des statistiques du graphe sont en cours de traitement, merci de patienter.");
            //A la fin des calculs :
            statsTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
                DecimalFormat df = new DecimalFormat("0.00");
                lbOrdre.setText(String.valueOf(g.bd.getOrdre()));
                lbDegreMoy.setText(String.valueOf(df.format(g.bd.getDegreeMoyen())));
                lbVolume.setText(String.valueOf(g.bd.getVolume()));
                if (g.bd.getDiametre() == Double.POSITIVE_INFINITY) lbDiametre.setText("+∞");
                else lbDiametre.setText(String.valueOf(g.bd.getDiametre()));
                setTab(g.bd.UserCentraux());
            });
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
    public void importPane(){
        pnCalculs.setVisible(false);
        //pnGraphe.setVisible(false);
        pnAffichage.setVisible(false);
        pnImport.setVisible(true);
        bAnnuler.setDisable(true);
    }

    public void affichagePane(){
        pnCalculs.setVisible(false);
        //pnGraphe.setVisible(false);
        pnImport.setVisible(false);
        pnAffichage.setVisible(true);
        if(g!=null)
            afficherTweets();
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    public void calculsPane(){
        pnAffichage.setVisible(false);
        //pnGraphe.setVisible(false);
        pnImport.setVisible(false);
        pnCalculs.setVisible(true);
        if(g!=null)
            this.calculs();
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    //Affichage du graphe
    /*public void graphePane(){
       // pnAffichage.setVisible(false);
       // pnCalculs.setVisible(false);
       // pnImport.setVisible(false);
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
    }*/

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
