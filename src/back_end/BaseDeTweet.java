package back_end;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDeTweet {

    //Attributs
    private HashMap<String, List<Tweet>>  baseTweet=new HashMap();
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
                if(data.length==5) t=new Tweet(data[0],data[1],data[2],data[3],data[4]);
                if(data.length==4) t=new Tweet(data[0],data[1],data[2],data[3]);
                if(data.length !=4 && data.length !=5){
                    System.out.println("Problème de longueur de data. !!!");
                    break;
                }

                List<Tweet> listeDeTweet;
                if(baseTweet.get(data[0])==null){
                    System.out.println("?");
                    listeDeTweet=new ArrayList<Tweet>();
                }
                else{
                    System.out.println("??");
                    listeDeTweet=baseTweet.get(data[0]);
                }
                listeDeTweet.add(t);
                baseTweet.put(data[0],listeDeTweet);
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
    public String toString() {
        System.out.println("Passage toString");
        String data="fin";

        for(Map.Entry me : baseTweet.entrySet()){

            //TODO refaire ça proprement, sans copie de var
            ArrayList<Tweet> liste=(ArrayList<Tweet>)me.getValue();
            int i=liste.size();
            System.out.println(me.getKey()+"      "+i);
        }
        return data;
    }
}


