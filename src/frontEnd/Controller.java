package frontEnd;

import back_end.Graphe;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;

public class Controller {

    @FXML
    JFXRadioButton rdClimat, rdFoot;

    //TODO : TreeTableColumns à ajouter par programmation

    public void selectedDataset(){
        if(rdClimat.isSelected()){
            System.out.println("données importées");
            new Graphe("src/data/climat.txt");
            System.out.println("données importées");
        }
        else
            if(rdFoot.isSelected()){
                new Graphe("src/data/foot.txt");
                System.out.println("données importées");
            }
    }
}
