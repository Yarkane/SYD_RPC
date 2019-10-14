//package rpc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Compilo {
  public static void main(String [] arg) throws IOException {
    //Gestion des arguments :
    if (arg.length != 2) {
      System.out.println("Bad usage : Compilateur <nom_du_code_java_de_l_interface> <port>");
    }
    String filename = arg[0];
    String port = arg[1];

    //Création du reader de l'interface
    FileReader readerIfc = new FileReader(filename);
    BufferedReader brIfc = new BufferedReader(readerIfc);


    //Noms des méthodes
    String[][] methods;

    //Arguments des méthodes
    String[][] methodsArg;

    //Retour des méthodes
    String[][] methodsRet;

    //Noms des classes
    String[][] classes;

    //Lecture des méthodes de l'interface de Matlab

    /*
    String line;
    while ((line = br.readLine()) != null) {
      if(line.contains('(')){
        String[][] elements = line.split(" ");
        for (int i; i<elements.length; i++){
          //...

          }
        }
      }
    }
    */

    //On analyse l'interface, et on obtient :
    methods[0] = "constructeur";
    methodsArg[0] = "int";
    methodsRet[0] = "";

    methods[1] = "calcul";
    methodsArg[1] = "int";
    methodsArg[1] = "Result";

    classes[0] = "Matlab";
    classes[1] = "Result";

    //On sait aussi qu'il existe, nécessairement, une classe client
    

    //UNE FOIS QU'ON A LES NOMS DES CLASSES
    //Création des readers
    /*
    FileReader readerClient = new FileReader(+".java");
    BufferedReader brClient = new BufferedReader(readerClient);

    FileReader readerMatlab = new FileReader("Matlab.java");
    BufferedReader brMatlab = new BufferedReader(readerMatlab);

    FileReader readerResult = new FileReader("Result.java");
    BufferedReader brResult = new BufferedReader(readerResult);

    //Création des Writers
    BufferedWriter writerClient = new BufferedWriter(new FileWriter("Client_RPC.java"));
    BufferedWriter writerMatlab = new BufferedWriter(new FileWriter("Matlab_RPC.java"));
    BufferedWriter writerResult = new BufferedWriter(new FileWriter("Result_RPC.java"));
    */
 }
}
