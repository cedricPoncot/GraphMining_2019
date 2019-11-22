package java;

import java.io.*;
import java.util.HashMap;
import java.util.List;

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
               /* for(int i =0;i<data.length;i++) {
                    System.out.println(data[i]);
                }*/
                Tweet t = null;
                if(data.length==5) t=new Tweet(data[0],data[1],data[2],data[3],data[4]);
                if(data.length==4) t=new Tweet(data[0],data[1],data[2],data[3]);
                if(data.length !=4 && data.length !=5){
                    System.out.println("Problème de longueur de data. !!!");
                    break;
                }
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
}


