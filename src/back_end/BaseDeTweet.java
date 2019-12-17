package back_end;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDeTweet {

    //Attributs
    private HashMap<String, List<Tweet>>  baseTweet=new HashMap();
    private HashMap <String,HashMap<String,Integer>> baseLink=new HashMap();
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
    //aaaab

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
                    if(baseLink.get(data[4])==null ){
                            HashMap<String, Integer> retweeter = new HashMap();
                            retweeter.put(data[1], 1);
                            baseLink.put(data[4], retweeter);

                    }
                    else{
                        if(baseLink.get(data[4]).get(data[1])==null){
                            HashMap<String, Integer> retweeter = baseLink.get(data[4]);
                            retweeter.put(data[1], 1);
                            baseLink.put(data[4], retweeter);
                        }
                        else{
                            int value = baseLink.get(data[4]).get(data[1]) + 1;
                            baseLink.get(data[4]).put(data[1], value);
                        }

                    }
                }
                if(data.length==4) t=new Tweet(data[0],data[1],data[2],data[3]);
                if(data.length !=4 && data.length !=5){
                    System.out.println("Problème de longueur de data. !!!");
                    break;
                }
                /*
                List<Tweet> listeDeTweet;
                if(baseTweet.get(data[1])==null){
                    listeDeTweet=new ArrayList<Tweet>();
                }
                else{
                    listeDeTweet=baseTweet.get(data[1]);
                }
                listeDeTweet.add(t);
                baseTweet.put(data[1],listeDeTweet);
                */
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
        /*
        for(Map.Entry me : baseLink.entrySet()){
            for(Map.Entry me2 : baseLink.get(me.getKey()).entrySet()){
                System.out.println("->"+me.getKey()+" _ "+me2.getKey()+" "+me2.getValue());
            }
        }
        */
        int maxRT=0;
        String UtilisateurMaxRT="";
        for(Map.Entry me : baseLink.entrySet()){
            int cmptr=0;
            for(Map.Entry me2 : baseLink.get(me.getKey()).entrySet()){
                cmptr+=(int)me2.getValue();
                System.out.println("->"+me.getKey()+" _ "+me2.getKey()+" "+me2.getValue());
            }
            if(baseLink.get(me.getKey()).size()>maxRT){
                maxRT=cmptr;
                UtilisateurMaxRT=(String)me.getKey();
            }
        }
        System.out.println("\nMax RT="+maxRT+" by "+ UtilisateurMaxRT);
        return "";
    }
}


