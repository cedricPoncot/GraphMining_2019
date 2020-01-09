package back_end;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tweet {
    /***************************************************ATTRIBUTS****************************************************/
    private BigInteger identifiant;
    private String tweeter;
    private LocalDateTime date;
    private String texte;
    private String retweeter;

    /*************************************************CONSTRUCTEURS**************************************************/
    public Tweet(String identifiant,String tweeter,String date,String texte,String retweeter){
        this.identifiant=new BigInteger(identifiant);
        this.tweeter=tweeter;
        DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.date=LocalDateTime.parse(date,format);
        this.texte=texte;
        this.retweeter=retweeter;
    }

    public Tweet(String identifiant,String tweeter,String date,String texte){
        this.identifiant=new BigInteger(identifiant);
        this.tweeter=tweeter;
        DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.date=LocalDateTime.parse(date,format);
        this.texte=texte;
    }

    /**********************************************GETTERS ET SETTERS**********************************************/
    public String getRetweeter() {
        return retweeter;
    }

    public String getTweeter() {
        return tweeter;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public BigInteger getIdentifiant() {
        return identifiant;
    }

    public String getTexte() {
        return texte;
    }

    /**************************************************FONCTIONS**************************************************/
    public String toString(){
        if(retweeter==null) {
            return identifiant + "\t" + tweeter + "\t" + date + "\t" + texte;
        }
        else{
            return identifiant + "\t" + tweeter + "\t" + date + "\t" + texte+"\t"+retweeter;
        }
    }
}
