package back_end;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportTask extends Task<ArrayList> {
    private String cheminCsv;
    private int nbLignes;

    public ImportTask(String cheminCsv, int nbLignes){
        this.cheminCsv = cheminCsv;
        this.nbLignes = nbLignes;
    }

    @Override
    protected ArrayList call() throws Exception {
        BufferedReader br=null;
        String line= "";
        ArrayList<Tweet> tweets = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(cheminCsv));
            int compteurLigne = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                compteurLigne++;
                if(data.length==5)
                    tweets.add(new Tweet(data[0], data[1], data[2], data[3], data[4]));
                else {
                    if (data.length == 4)
                        tweets.add(new Tweet(data[0], data[1], data[2], data[3]));
                }
                this.updateProgress(compteurLigne, nbLignes);
                charger(compteurLigne);
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
        return tweets;
    }

    private void charger(int ligneCourante) throws InterruptedException {
        this.updateMessage(ligneCourante+" sur "+nbLignes+" tweets");
        //Thread.sleep(0,1);
    }

}
