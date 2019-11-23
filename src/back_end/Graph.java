package back_end;

public class Graph {


        public Graph() {
            System.out.println("Entrée Import");
            //Création de la base de tweet
            String cheminCSV = "src/data/climat.txt";
            new BaseDeTweet(cheminCSV, "base");
            //sc.close();
        }

    }



