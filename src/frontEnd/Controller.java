package frontEnd;

import back_end.Centrality;
import back_end.Graphe;
import back_end.Tweet;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeSet;

public class Controller {
    @FXML
    JFXRadioButton rdClimat, rdFoot;

    @FXML
    TableView table;

    @FXML
    TableColumn col1, col2;

    @FXML
    JFXButton bVisualiser, bCalculer;

    @FXML
    Label lbOrdre, lbVolume, lbDiametre, lbDegreMoy;

    @FXML
    AnchorPane pnListeTweets, pnAffichage, pnCalculs;

    private byte dataset = 0; //1: climat 2 : foot
    private int x=0, y=0; //position du tweet

    //fonctions
    public void calculs(){
        if(dataset==1){
            affichageCalculs(new Graphe("src/data/climat.txt"));
        }
        else{
            if(dataset==2){
                affichageCalculs(new Graphe("src/data/foot.txt"));
            }
        }
    }

    public void affichageCalculs(Graphe g){
        DecimalFormat df = new DecimalFormat("0.00");
        lbOrdre.setText(String.valueOf(g.bd.getOrdre()));
        lbDegreMoy.setText(String.valueOf(df.format(g.bd.getDegreeMoyen())));
        lbVolume.setText(String.valueOf(g.bd.getVolume()));
        if(g.bd.getDiametre()==Double.POSITIVE_INFINITY) lbDiametre.setText("+∞");
        else lbDiametre.setText(String.valueOf(g.bd.getDiametre()));
    }

    public void selectedDataset(){
        if(rdClimat.isSelected())
            dataset = 1;
        else
            dataset = 2;
    }

    public void setTab(TreeSet<Centrality> tab) {
        ObservableList<Centrality> usersCentreaux = FXCollections.observableArrayList(tab);
        table.setItems(usersCentreaux);
    }

    //Methodes d'affichages des panes
    public void affichagePane(){
        pnCalculs.setVisible(false);
        pnAffichage.setVisible(true);
        //afficherTweets(g);
    }
    public void calculsPane(){
        pnAffichage.setVisible(false);
        pnCalculs.setVisible(true);
        calculs();
    }

    //Affichage des tweets
    public void afficherTweets(Graphe g){
        ArrayList<Tweet> liste = g.bd.getTweets();
        for(Tweet t : liste){
            if(t.getRetweeter().isEmpty()) {
                String time = t.getDate().getHour() + ":" + t.getDate().getMinute();
                String date = t.getDate().getDayOfMonth() + "/" + t.getDate().getMonthValue() + "/" + t.getDate().getYear();
                pnListeTweets.getChildren().add(new JFXTweet(t.getTweeter(), t.getTexte(), time, date, 0, x, y));
                y += 180;
            }
        }
    }

    //Fonctions FXML
    @FXML
    public void initialize() {
        col1.setText("Utilisateur");
        col2.setText("Poids");
        col1.setCellValueFactory(new PropertyValueFactory<Centrality, String>("nom"));
        col2.setCellValueFactory(new PropertyValueFactory<Centrality, String>("poids"));
    }
}
