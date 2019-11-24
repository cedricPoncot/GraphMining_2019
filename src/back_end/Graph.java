package back_end;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;

import java.io.*;
import java.net.URI;
import java.util.*;

public class Graph {


        public Graph() {
            System.out.println("Début Import");
            //Création de la base de tweet
            String cheminCSV = "src/data/climat.txt";
            BaseDeTweet bd=new BaseDeTweet(cheminCSV, "base");
            System.out.println(bd);
            //sc.close();
        }

    }



