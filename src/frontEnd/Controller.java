package frontEnd;

import back_end.Centrality;
import back_end.Graphe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DecimalFormat;
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

    private byte dataset = 0; //1: climat 2 : foot
    //TODO : TreeTableColumns à ajouter par programmation

    public void calculs(){
        Graphe g;
        DecimalFormat df = new DecimalFormat("0.00");
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
        if(g.bd.getDiametre()<0) lbDiametre.setText("+∞");
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

    //Fonctions FXML
    @FXML
    public void initialize() {
        col1.setText("Utilisateur");
        col2.setText("Poids");
        col1.setCellValueFactory(new PropertyValueFactory<Centrality, String>("nom"));
        col2.setCellValueFactory(new PropertyValueFactory<Centrality, String>("poids"));
    }
}
