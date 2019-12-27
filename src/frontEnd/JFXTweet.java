package frontEnd;

import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class JFXTweet {
    private AnchorPane pane;
    private JFXTextArea texte;
    private Label nbRetweets, date, time, retweeters, username;
    private ImageView idImage;

    public JFXTweet(String username, String texte, String time, String date, String nbRetweets, int x, int y){
        //OBJETS
        pane = new AnchorPane();
        pane.setPrefSize(480,175);
        this.texte = new JFXTextArea();
        this.texte.setPrefSize(400,60);
        idImage = new ImageView();
        idImage.setFitHeight(35);
        idImage.setFitWidth(35);
        this.username = new Label();
        retweeters = new Label("Retweeters");
        this.time = new Label();
        this.date = new Label();
        this.nbRetweets = new Label();

        //POSITION
        pane.setLayoutX(x);
        pane.setLayoutY(y);
        this.texte.setLayoutX(63);
        this.texte.setLayoutY(57);
        this.username.setLayoutX(63);
        this.username.setLayoutY(23);
        this.nbRetweets.setLayoutX(395);
        this.nbRetweets.setLayoutY(129);
        retweeters.setLayoutX(415);
        retweeters.setLayoutY(129);
        this.time.setLayoutX(63);
        this.time.setLayoutY(129);
        this.date.setLayoutX(115);
        this. date.setLayoutY(129);
        idImage.setLayoutX(14);
        idImage.setLayoutY(14);

        //CONTENU
        this.username.setText(username);
        this.nbRetweets.setText(nbRetweets);
        this.time.setText(time);
        this.date.setText(date);
        this.texte.setText(texte);
    }

}
