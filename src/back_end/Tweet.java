package back_end;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tweet {
    BigInteger identifiant;
    String tweeter;
    LocalDateTime date;
    String texte;
    String retweeter;

    Tweet(String identifiant,String tweeter,String date,String texte,String retweeter){
        this.identifiant=new BigInteger(identifiant);
        this.tweeter=tweeter;
        DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.date=LocalDateTime.parse(date,format);
        this.texte=texte;
        this.retweeter=retweeter;
    }
    Tweet(String identifiant,String tweeter,String date,String texte){
        this.identifiant=new BigInteger(identifiant);
        this.tweeter=tweeter;
        DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        this.date=LocalDateTime.parse(date,format);
        this.texte=texte;
    }

    public String toString(){
        if(retweeter==null) {
            return identifiant + "\t" + tweeter + "\t" + date + "\t" + texte;
        }
        else{
            return identifiant + "\t" + tweeter + "\t" + date + "\t" + texte+"\t"+retweeter;
        }
    }
}
