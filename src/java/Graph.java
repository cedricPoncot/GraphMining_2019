package java;

import java.io.IOException;
import java.util.Scanner;

public class Graph {

    public static void main(String[] args) {

        //Création de la base de tweet
        //Scanner sc = new Scanner(System.in);
        String cheminCSV="D:/GraphMining/Ressources/climat.txt";
        new BaseDeTweet(cheminCSV,"base");
        //sc.close();

    }
}
