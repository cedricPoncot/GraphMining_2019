package back_end;

import javax.jws.soap.SOAPBinding;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDeTweet {

    class CoupleStringOrdonne {
        String UserInitial;
        String UserFinal;
        CoupleStringOrdonne(String UserInitial,String UserFinal){
            this.UserInitial=UserInitial;
            this.UserFinal=UserFinal;
        }
        public String toString(){
            return UserInitial+" has RT "+UserFinal;
        }
    }

    //Attributs
    private HashMap<String, List<Tweet>>  baseTweet=new HashMap();
    private HashMap <CoupleStringOrdonne,Integer> listLink=new HashMap();
    private String nomBase;


    //Constructeurs
    BaseDeTweet(String cheminCSV){
        nomBase="undefined";
        try {
            importCSV(cheminCSV);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public BaseDeTweet(String cheminCSV, String nomBase){
        this.nomBase=nomBase;
        try {
            importCSV(cheminCSV);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    //Getter / Setter
    public String getNomBase(){
        return nomBase;
    }

    public HashMap<String, List<Tweet>> getBaseTweet() {
        return baseTweet;
    }

    //Fonctions
    void importCSV(String cheminCSV) throws FileNotFoundException {
        BufferedReader br=null;
        String line="";
        try {
            br=new BufferedReader(new FileReader(cheminCSV));
            int compteur_ligne=0;
            while((line=br.readLine())!=null){
                String[] data=line.split("\t");



                Tweet t = null;
                if(data.length==5){
                    t=new Tweet(data[0],data[1],data[2],data[3],data[4]);

                    CoupleStringOrdonne key= new CoupleStringOrdonne(data[1],data[4]);

                    //Si le lien n'existe pas, on le créer
                    if(listLink.get(key)==null){
                        listLink.put(key,1);
                    }
                    //Sinon on incrémente le poids
                    else{
                        System.out.println("2 rt");
                        int poids=listLink.get(key);
                        listLink.put(key,poids+1);
                        break;
                    }

                }
                if(data.length==4) t=new Tweet(data[0],data[1],data[2],data[3]);
                if(data.length !=4 && data.length !=5){
                    System.out.println("Problème de longueur de data. !!!");
                    break;
                }

                List<Tweet> listeDeTweet;
                if(baseTweet.get(data[1])==null){
                    listeDeTweet=new ArrayList<Tweet>();
                }
                else{
                    listeDeTweet=baseTweet.get(data[1]);
                }
                listeDeTweet.add(t);
                baseTweet.put(data[1],listeDeTweet);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                try{
                    br.close();
                }
                catch ( IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    //TODO IMPLEM
    public String toString() {
        System.out.println("Fin import");
        String data="";
        /*
        for(Map.Entry me : baseTweet.entrySet()){
            System.out.println("");
            ArrayList<Tweet> liste=(ArrayList<Tweet>)me.getValue();
            System.out.println("User"+me.getKey()+"   Nombre de tweet :"+liste.size());
            for(Tweet t:liste){
                System.out.println(t);
            }
        }

        */
        for(Map.Entry me : listLink.entrySet()){
            System.out.println(me.getKey()+"->"+me.getValue());
        }
        return "";
    }
}


