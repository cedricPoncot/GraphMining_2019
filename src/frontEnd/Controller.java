package frontEnd;

import back_end.Centrality;
import back_end.Graphe;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.util.TreeSet;

public class Controller {
    public class lineTab{
        String nom;
        int poids;
        public lineTab(String nom, int poids){
            this.nom=nom;
            this.poids=poids;
        }
    }
    @FXML
    JFXRadioButton rdClimat, rdFoot;

    @FXML
    //JFXTreeTableView<lineTab> tableView;
    JFXTreeTableView tableView;

    //TODO : TreeTableColumns à ajouter par programmation

    public void selectedDataset(){
        Graphe g;
        if(rdClimat.isSelected()){
            System.out.println("données importées");
            g=new Graphe("src/data/climat.txt");
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
    public void setTab(TreeSet<Centrality> tab){
        /*
        tableView=new JFXTreeTableView();

        TreeTableColumn<lineTab,String> firstCol=new TreeTableColumn("Utilisateur");
        firstCol.setPrefWidth(200);
        firstCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<lineTab,String> param)->{return param.getValue().getValue().nom;});

        TreeTableColumn<lineTab,String> secondCol=new TreeTableColumn("Nb de RT");
        secondCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<lineTab,String> param)->{return param.getValue().getValue().poids;});

        tableView.getColumns().addAll(firstCol,secondCol);

        ObservableList<lineTab> users = FXCollections.observableArrayList();
        for(Centrality i:tab){
            users.add(new lineTab(i.nom,i.poids));
        }

        final TreeItem<lineTab> root=new TreeItem<lineTab>(new lineTab("a",1));


        //JFXTreeTableView<Centrality> treeView = new JFXTreeTableView<>(root);
*/
    }
}
