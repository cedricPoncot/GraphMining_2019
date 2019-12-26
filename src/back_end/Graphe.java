package back_end;

public class Graphe {
   public BaseDeTweet bd;

        public Graphe(String path) {
            System.out.println("Entrée Import");
            //Création de la base de tweet
            String cheminCSV = path;
             bd=new BaseDeTweet(cheminCSV, "base");
            System.out.println(bd);
            //sc.close();
        }

    }



