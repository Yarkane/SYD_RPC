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


    //Noms des méthodes. 6 Maximum.
    String[] methods;
    methods = new String[5];

    //Arguments des méthodes
    String[] methodsArg;
    methodsArg = new String[5];

    //Retour des méthodes
    String[] methodsRet;
    methodsRet = new String[5];

    //Noms des classes. 6 Maximum.
    String[] classes;
    classes = new String[5];

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

    classes[0] = "Matlab"; //Quel que soit son nom, la classe 0 sera forcément celle implémentant l'interface
    classes[1] = "Result";

    //On sait aussi qu'il existe, nécessairement, une classe client

    classes[2] = "Client";


    //UNE FOIS QU'ON A LES NOMS DES CLASSES
    //Création des readers et writers

    BufferedReader[] readers;
    readers = new BufferedReader[classes.length-1];

    BufferedWriter[] writers;
    writers = new BufferedWriter[classes.length-1];

    for(int i=0; i<classes.length;i++){
      FileReader readerClient = new FileReader(classes[i] +".java");
      readers[i] = new BufferedReader(readerClient);

      writers[i] = new BufferedWriter(new FileWriter( "v2/"+ classes[i] + ".java"));
    }

    //Edition du fichier matlab.java (celui correspondant à l'interface) :

    //On lit/recopie jusqu'à trouver le début de la classe.
    String line;
    while ((line = readers[0].readLine()) != null) {
      if (line.contains("class " + classes[0])) {
        //On écrit un main()
        writers[0].write("    " + classes[0] + " m = null;\n");
        writers[0].write("    java.net.ServerSocket sos = new java.net.ServerSocket(" + port + ");\n");
        writers[0].write("    java.net.Socket s = sos.accept();\n");
        writers[0].write("    java.io.DataInputStream dis = new java.io.DataInputStream(s.getInputStream());\n");
        writers[0].write("    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());\n");
        writers[0].write("    String fonction = dis.readUTF();\n");
        //A présent, on inclut les méthodes.
        for (int j = 0; j < methods.length; j++) {
          writers[0].write("        if (fonction.equals(\"" + methods[j] + "\")) {\n");
          //SI CONSTRUCTEUR : Syntaxe précise
          if (methods[j].equals("constructeur")) writers[0].write("      m = new " + classes[0] + "(dis.");
            //Sinon, on considère que la fonction retourne un objet.
          else writers[0].write("      oos.writeObject(m.calcul(dis.");

          //A présent, que faut-il lire ? L'entrée dépend de l'argument de la fonction.
          if (methodsArg[j].equals("int")) writers[0].write("readInt());\n");
          else if (methodsArg[j].equals("float")) writers[0].write("readFloat());\n");
          else if (methodsArg[j].equals("char")) writers[0].write("readChar());\n");
          else writers[0].write("readByte());\n");

          //On clot la fonction.
          writers[0].write("    }\n");
        }
        //On clot le main
        writers[0].write("  }\n");

        //Après, il suffit de recopier les autres méthodes du fichier de base.
        while ((line = readers[0].readLine()) != null) {
          writers[0].write(line + "\n");
        }
      } else writers[0].write(line + "\n");
    }


    //Fermeture des writers :

    for(int i=0; i<writers.length; i++) writers[i].close();

 }
}
