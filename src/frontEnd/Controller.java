package frontEnd;

import back_end.Centrality;
import back_end.Graphe;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.TreeSet;

public class Controller {
    @FXML
    JFXRadioButton rdClimat, rdFoot;

    @FXML
    TableView table;

    @FXML
    TableColumn col1, col2;

    //TODO : TreeTableColumns à ajouter par programmation

    public void selectedDataset(){
        Graphe g;
        if(rdClimat.isSelected()){
            System.out.println("données importées");
            g = new Graphe("src/data/climat.txt");
            System.out.println("données importées");
            setTab(g.bd.UserCentraux());

        }
        else {
            if (rdFoot.isSelected()) {
                g = new Graphe("src/data/foot.txt");
                System.out.println("données importées");
                setTab(g.bd.UserCentraux());
            }
        }

    }

    @FXML
    public void initialize() {
        col1.setText("Utilisateur");
        col2.setText("Poids");
        col1.setCellValueFactory(new PropertyValueFactory<Centrality, String>("nom"));
        col2.setCellValueFactory(new PropertyValueFactory<Centrality, String>("poids"));
    }

    public void setTab(TreeSet<Centrality> tab){
        initialize();
        ObservableList<Centrality> usersCentreaux = FXCollections.observableArrayList();
        usersCentreaux.addAll(tab);
        System.out.println(usersCentreaux);
        table.setItems(usersCentreaux);
    }
}
