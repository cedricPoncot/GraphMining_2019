package frontEnd;

import back_end.*;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
//import org._3pq.jgrapht.edge.DefaultEdge;
//import org.jgrapht.graph.DefaultEdge;

import javafx.scene.layout.HBox;

import java.text.DecimalFormat;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Controller {
    /* Calsse Controller : cette classe relie le code du backend à l'interface utilisateur.
     * Elle modifie aussi les valeurs des éléments de la GUI.
     * Le positionnement des éléments de la GUI est détaillé dans le fichier FXML.
     * Le style de ces éléments est détaillé dans la feuille de style : style.css */

    /*****************************************************************************************/
    /***************************************ELEMENTS FXML*************************************/
    /*****************************************************************************************/
    @FXML
    JFXRadioButton rdClimat, rdFoot;

    @FXML
    TableView table, tableAffichage;

    @FXML
    TableColumn col1, col2, col3, col4, col5, col6, col7;

    @FXML
    JFXButton bVisualiser, bCalculer, bImport, bAnnuler, bUsersCentraux;

    @FXML
    Label lbOrdre, lbVolume, lbDiametre, lbDegreMoy, lbProgressStatus, lbSelectedDataset;

    @FXML
    AnchorPane pnGraphe, pnAffichage, pnCalculs, pnImport, pnCalculsProgress;

    @FXML
    JFXProgressBar importProgress;

    @FXML
    ProgressIndicator calculsProgress;

    @FXML
    JFXTextArea txtDescriptionDonnees;

    @FXML
    HBox menuBox;

    @FXML
    JFXTextField txtNbUsersCentraux;

    /*****************************************************************************************/
    /*********************************AUTRES VARIABLES UTILES*********************************/
    /*****************************************************************************************/
    private byte dataset = 0; //1: climat 2 : foot
    BaseDeTweet bd = null;
    private final int nbTweetsClimat = 1977769, nbTweetsFoot = 899597; //Nombre de lignes par fichier
    private final String pathClimat = "src/data/climat.txt", pathFoot = "src/data/foot.txt"; //Chemin des fichiers de données
    private final String descriptionFoot = "Ce jeu de données met à votre disposition :\n- 899597 tweets.\n- Les tweets sélectionnés contiennent les mots clés suivants : " +
            "\"climat\", \"climatique\", \"environnement\", \"environnemental\" et \"environnementaux\".\n- Les tweets datent du 02/09/2019.\n" +
            "- Chaque tweet est présenté sous la forme suivante : \n\tIDTweet\tUserID\tDate de publilcation\tTexte\tRetweeterID\n" +
            "\tOù :\n\t\tIDTweet : représente le numéro d'identification du tweet.\n" +
            "\t\tUserID : représente le nom d'utilisateur de la personne qui a tweeté le tweet courant.\n" +
            "\t\tDate de publilcation : représente la date de publication du tweet.\n" +
            "\t\tTexte : contient le contenu du tweet, hashtag compris.\n" +
            "\t\tRetweeterID : représente le nom d'utilisateur de la personne qui a retweeté le tweet courant.", //description du jeu de données Foot

            descriptionClimat = "Ce jeu de données met à votre disposition :\n- 1977769 tweets.\n- Les tweets sélectionnés contiennent les mots clés suivants : " +
            "\"foot\", \"football\", \"#WWC2019\", \"#CM2019\", \"#FootFeminin\" et \"#FIFAWWC\".\n" +
            "- Les tweets ont été récoltés durant la période du 21/06/2019 au 10/07/2019 pendant la phase ﬁnale de la coupe du monde féminine.\n" +
            "- Chaque tweet est présenté sous la forme suivante : \n\tIDTweet\tUserID\tDate de publilcation\tTexte\tRetweeterID\n" +
            "\tOù :\n\t\tIDTweet : représente le numéro d'identification du tweet.\n" +
            "\t\tUserID : représente le nom d'utilisateur de la personne qui a tweeté le tweet courant.\n" +
            "\t\tDate de publilcation : représente la date de publication du tweet.\n" +
            "\t\tTexte : contient le contenu du tweet, hashtag compris.\n" +
            "\t\tRetweeterID : représente le nom d'utilisateur de la personne qui a retweeté le tweet courant.";//description du jeu de données Climat

    /*****************************************************************************************/
    /*************************************FONCTIONS UTILES************************************/
    /*****************************************************************************************/
    //Action du bouton importer
    public void importerDonnees() {
        //Modification des propriétés des éléments de la page d'import des données
        bImport.setDisable(true);
        bAnnuler.setDisable(false);
        menuBox.setDisable(true);

        String path = ""; //Chemin du fichier
        int nbTweets=0; //Nombre de tweets du fichier

        //sélection du dataset
        if(dataset==1){
            path = pathClimat;
            nbTweets = nbTweetsClimat;
        }
        else{
            if(dataset==2){
                path = pathFoot;
                nbTweets= nbTweetsFoot;
            }
            else
                errorDialog("Dataset non sélectionné !", "Veuillez sélectionner un dataset à importer.");
        }

        //Création de la base de tweets
        bd = new BaseDeTweet();

        //Import des données
        ImportTask importTask = new ImportTask(path, nbTweets); // création de la tâche d'import des données

        //Modification des propriétés des éléments de la page d'import des données
        importProgress.setVisible(true);
        importProgress.setProgress(0);
        lbProgressStatus.setVisible(true);
        importProgress.progressProperty().unbind();
        importProgress.progressProperty().bind(importTask.progressProperty()); //Relier la progress bar à la progression de la tâche d'import des données
        lbProgressStatus.textProperty().unbind();
        lbProgressStatus.textProperty().bind(importTask.messageProperty()); //Relier e message à afficher à la progression de la tâche d'import des données

        new Thread(importTask).start(); // création et lancement du thread d'import des données

        //Lorsque la tâche d'import est terminée :
        importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            bd.setTweets(importTask.getValue());//Quand l'import est terminé : on récupère le résultat
            importDefaultPane("Chargement des données terminé."); //Retour à la page par défaut
            informationDialog("Données importées !", "Vous pouvez à présent afficher les données, afficher les statistiques ou faire du Clustering.");
        });

        //Annulation de l'import
        bAnnuler.setOnAction(event -> {
            importTask.cancel(true);
            bd = null;
            importDefaultPane("Chargement des données annulé."); //Retour à la page par défaut
        });

    }

    //Action du pane des calculs : calcul du volume, diamètre, ordre etc.
    private void calculs(){
        if(bd!=null) {
            //Modification des propriétés des éléments de la page de calcul des statistiques
            menuBox.setDisable(true);
            pnCalculsProgress.setVisible(true);

            //Classe  anonyme : création de la tâche qui fait les calculs
            Task calculsTask = new Task() {
                @Override
                protected Object call() {
                    bd.calculs();
                    return null;
                }
            };

            //Modification des propriétés des éléments de la page de calcul des statistiques
            calculsProgress.setProgress(0);
            calculsProgress.progressProperty().unbind();
            calculsProgress.progressProperty().bind(calculsTask.progressProperty()); //Relier la progress bar à la progression de la tâche de calcul

            new Thread(calculsTask).start(); //Lancer le thread des calculs

            pnCalculs.setDisable(true);

            //A la fin des calculs :
            calculsTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
                afficherCalculs(); //Afficher les résultats des caculs effectués
                calculUsersCentraux(); //Afficher les utilisateurs centraux
                calculsDefaultPane(); //Retour aux paramètres par défaut de la page
            });
        }
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    public void calculUsersCentraux(){
        int nb = checkNbUsersCentraux();
        if(nb>0)
            afficherUsersCentraux(bd.UserCentraux(nb));
    }

    private int checkNbUsersCentraux(){
        String nb = txtNbUsersCentraux.getText();
        if(nb.isEmpty())
            return 5; //Valeur par défaut
        else {
            // Vérification sur la saisie
            try{
                int nombre=parseInt(nb);
                if(nombre<0 || nombre>10000){
                    errorDialog("Nombre qui n'est pas dans l'intervalle [0,10000] !", "Entrez un nombre correct.");
                    return -1;
                }
                return nombre;
            }
            catch (NumberFormatException e){
                errorDialog("Nombre invalide !", "Entrez un nombre correct.");
                return -1;
            }
            /*
            String regex = "[1-9]{1-9}*"; //Nombres entre 1 et 99999
            Pattern pattern = Pattern.compile(nb);
            Matcher matcher = pattern.matcher(regex);
            if (matcher.matches())
                return parseInt(nb);
            else {
                errorDialog("Nombre invalide !", "Entrez un nombre correct.");
                return -1;
            }
            */
        }
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

    /*****************************************************************************************/
    /**********************FONCTIONS QUI MODIFIENT LES ELEMENTS DE LA GUI*********************/
    /*****************************************************************************************/
    private void importDefaultPane(String message){
        //Page d'import par défaut
        bImport.setDisable(false);
        menuBox.setDisable(false);
        bAnnuler.setDisable(true);
        importProgress.progressProperty().unbind();
        importProgress.setProgress(0);
        importProgress.setVisible(false);
        lbProgressStatus.textProperty().unbind();
        lbProgressStatus.setText(message);
    }

    private void calculsDefaultPane(){
        //Page de calculs par défaut
        calculsProgress.progressProperty().unbind();
        calculsProgress.setProgress(0);
        pnCalculsProgress.setVisible(false);
        menuBox.setDisable(false);
        pnCalculs.setDisable(false);
    }

    //Affichage des calculs sur la GUI
    private void afficherCalculs(){
        if(dataset==1)
            lbSelectedDataset.setText("Climat");
        else
            lbSelectedDataset.setText("Foot");
        DecimalFormat df = new DecimalFormat("0.00");
        lbOrdre.setText(String.valueOf(bd.getOrdre()));
        lbDegreMoy.setText(String.valueOf(df.format(bd.getDegreeMoyen())));
        lbVolume.setText(String.valueOf(bd.getVolume()));
        if (bd.getDiametre() == Double.POSITIVE_INFINITY)
            lbDiametre.setText("+∞");
        else
            lbDiametre.setText(String.valueOf(bd.getDiametre()));
    }
    
    //Sélection du jeu de données
    public void selectedDataset(){
        if(rdClimat.isSelected()) {
            dataset = 1;
            txtDescriptionDonnees.setText(descriptionClimat);
        }
        else {
            dataset = 2;
            txtDescriptionDonnees.setText(descriptionFoot);
        }
    }

    //Affichage des users centraux
    private void afficherUsersCentraux(TreeSet<Centrality> tab) {
        ObservableList<Centrality> usersCentraux = FXCollections.observableArrayList(tab);
        table.setItems(usersCentraux);
    }

    //Afficher les tweets
    private void afficherTweets(){
        ObservableList<Tweet> liste = FXCollections.observableArrayList(bd.getTweets());
        tableAffichage.setItems(liste);
    }

    /*****************************************************************************************/
    /***********************************AFFICHAGE DES PANES***********************************/
    /*****************************************************************************************/
    //Pane d'import des données
    public void importPane(){
        pnCalculs.setVisible(false);
        //pnGraphe.setVisible(false);
        pnAffichage.setVisible(false);
        pnImport.setVisible(true);
        bAnnuler.setDisable(true);
        lbProgressStatus.setVisible(false);
        rdFoot.setSelected(false);
        rdClimat.setSelected(false);
        txtDescriptionDonnees.setText("");
    }

    //Pane d'affichage des données importées
    public void affichagePane(){
        pnCalculs.setVisible(false);
        //pnGraphe.setVisible(false);
        pnImport.setVisible(false);
        pnAffichage.setVisible(true);
        if(bd!=null)
            afficherTweets();
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    //Pane d'affichage et calul des statistiques du graphe
    public void calculsPane(){
        pnAffichage.setVisible(false);
        //pnGraphe.setVisible(false);
        pnImport.setVisible(false);
        pnCalculs.setVisible(true);
        if(bd!=null)
            this.calculs();
        else
            errorDialog("Données non importées !", "Veuillez importer les données avant de procéder aux calculs.");
    }

    /****************************************************************************************/
    /***********************************BOITES DE DIALOGUE***********************************/
    /****************************************************************************************/
    //Message d'information
    private void informationDialog(String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Message d'erreur
    private void errorDialog(String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERREUR");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /*****************************************************************************************/
    /*************************************FONCTIONS FXML**************************************/
    /*****************************************************************************************/
    //Initialisation des TableView de la GUI
    @FXML
    public void initialize() {
        //Initialisation de la table d'affichage des utilisateurs centraux
        col1.setCellValueFactory(new PropertyValueFactory<Centrality, String>("nom"));
        col2.setCellValueFactory(new PropertyValueFactory<Centrality, String>("poids"));
        //Initialisation de la table d'affichage des tweets
        col3.setCellValueFactory(new PropertyValueFactory<Tweet, String>("identifiant"));
        col4.setCellValueFactory(new PropertyValueFactory<Tweet, String>("tweeter"));
        col5.setCellValueFactory(new PropertyValueFactory<Tweet, String>("date"));
        col6.setCellValueFactory(new PropertyValueFactory<Tweet, String>("texte"));
        col7.setCellValueFactory(new PropertyValueFactory<Tweet, String>("retweeter"));
    }
}
