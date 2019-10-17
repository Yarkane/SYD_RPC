package rpc;

import javax.swing.plaf.synth.SynthTabbedPaneUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Compilo {
  public static void main(String [] arg) throws IOException {
    //Gestion des arguments :
    if (arg.length == 0 || !(arg.length == 2 || (arg.length==4 && arg[0].equals("-f")))){
      System.out.println("Bad usage : Compilateur [-f chemin_vers_les_classes_avec_/] <nom_du_fichier_java_de_l_interface> <port>");
      return;
    }
    String folder;
    String filename;
    String port;
    if (arg.length == 4) {
      folder = arg[1];
      filename = arg[2];
      port = arg[3];
    }else{
      folder = "";
      filename = arg[0];
      port = arg[1];
    }

    //Création du reader de l'interface
    FileReader readerIfc = new FileReader(folder + filename);
    BufferedReader brIfc = new BufferedReader(readerIfc);


    //Noms des méthodes.
    ArrayList<String> methods;
    methods = new ArrayList<String>();

    //Arguments des méthodes
    ArrayList<String> methodsArg;
    methodsArg = new ArrayList<String>();

    //Retour des méthodes
    ArrayList<String> methodsRet;
    methodsRet = new ArrayList<String>();

    //Noms des classes.
    ArrayList<String> classes;
    classes = new ArrayList<String>();

    //Nous allons réécrire le fichier interface.
    File dir = new File (folder + "/v2");
    dir.mkdirs();
    BufferedWriter bwIfc = new BufferedWriter(new FileWriter( folder + "v2/"+ filename));

    //Lecture des méthodes de l'interface de Matlab
    String line;
    while ((line = brIfc.readLine()) != null) {
      //Regular expression
      // ........
      if (line.contains("package")){
        int pointVirgule;
        pointVirgule = line.indexOf(";");
        bwIfc.write(line.substring(0,pointVirgule) + ".v2;\n");
      }
      else bwIfc.write(line + "\n");
    }


    //TODO : Analyse effective (choix fait de continuer avec les résultats que l'on devrait obtenir)
    //Il est possible de parvenir à ces résultats par le biais de RegExp.
    //Néanmoins, ne les maîtrisant pas, je préfère m'atteler à la partie "compilation" des fichiers.

    //On analyse l'interface, et on obtient :
    methods.add("constructeur");
    methodsArg.add("int");
    methodsRet.add("");

    methods.add("calcul");
    methodsArg.add("int");
    methodsArg.add("Result");

    classes.add("Matlab"); //Quel que soit son nom, la classe 0 sera forcément celle implémentant l'interface
    classes.add("Result");

    //On sait aussi qu'il existe, nécessairement, une classe client

    classes.add("Client");


    //UNE FOIS QU'ON A LES NOMS DES CLASSES
    //Création des readers et writers

    BufferedReader[] readers;
    readers = new BufferedReader[classes.size()];

    BufferedWriter[] writers;
    writers = new BufferedWriter[classes.size()];



    for(int i=0; i<classes.size();i++){
      FileReader readerClient = new FileReader(folder + classes.get(i) +".java");
      readers[i] = new BufferedReader(readerClient);

      writers[i] = new BufferedWriter(new FileWriter( folder + "v2/"+ classes.get(i) + ".java"));
    }

    //Edition du fichier matlab.java (celui correspondant à l'interface) :

    //On lit/recopie jusqu'à trouver le début de la classe.
    while ((line = readers[0].readLine()) != null) {
      if (line.contains("package")){
        int pointVirgule;
        pointVirgule = line.indexOf(";");
        writers[0].write(line.substring(0,pointVirgule) + ".v2;\n");
      }
      else if (line.contains("class " + classes.get(0))) {
        //On écrit un main()
        writers[0].write(line + "\n");
        writers[0].write("  public static void main(String [] arg) throws java.io.IOException {\n");
        writers[0].write("    " + classes.get(0) + " m = null;\n");
        writers[0].write("    java.net.ServerSocket sos = new java.net.ServerSocket(" + port + ");\n");
        writers[0].write("    java.net.Socket s = sos.accept();\n");
        writers[0].write("    java.io.DataInputStream dis = new java.io.DataInputStream(s.getInputStream());\n");
        writers[0].write("    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());\n");
        writers[0].write("    String fonction = dis.readUTF();\n");
        //A présent, on inclut les méthodes.
        for (int j = 0; j < methods.size(); j++) {
          writers[0].write("        if (fonction.equals(\"" + methods.get(j) + "\")) {\n");
          //SI CONSTRUCTEUR : Syntaxe précise
          if (methods.get(j).equals("constructeur")) writers[0].write("      m = new " + classes.get(0) + "(dis.");
            //Sinon, on considère que la fonction retourne un objet.
          else writers[0].write("      oos.writeObject(m.calcul(dis.");

          //A présent, que faut-il lire ? L'entrée dépend de l'argument de la fonction.
          if (methodsArg.get(j).equals("int")) writers[0].write("readInt())");
          else if (methodsArg.get(j).equals("float")) writers[0].write("readFloat())");
          else if (methodsArg.get(j).equals("char")) writers[0].write("readChar())");
          else writers[0].write("readByte())");
          //Si pas constructeur : on doit rajouter une parenthèse pour que la syntaxe soit correcte
          if (!methods.get(j).equals("constructeur")) writers[0].write(")");
          writers[0].write(";\n");

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

    //Transformation du code du client : toujours en dernière position.
    int nClient = readers.length - 1;
    //On initialise un nom de variable résultat qui pourra avoir à être remplacé.
    //On lui donne une valeur invalide pour commencer.
    String res = "/;;;;;;;;;/";
    while ((line = readers[nClient].readLine()) != null) {
      //Avant toute chose : si l'on a une variable "résultat", on lit sur la socket.
      line = line.replaceAll(res,"ois.readObject()");
      if (line.contains("package")){
        int pointVirgule;
        pointVirgule = line.indexOf(";");
        writers[nClient].write(line.substring(0,pointVirgule) + ".v2;\n");
      }
      //Le client comporte nécessairement un main
      else if(line.contains("public static void main(")){
        //Première étape : ouvrir socket
        writers[nClient].write("  public static void main(String [] arg) throws Exception {\n");
        writers[nClient].write("    java.net.Socket s = new java.net.Socket(\"\\\\localhost\\\\\", " + port + ");\n");
        writers[nClient].write("    java.io.DataOutputStream dos = new java.io.DataOutputStream(s.getOutputStream());\n");
        writers[nClient].write("    java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());\n");
      }
      //Il faut ensuite convertir les appels à des méthodes en appels à la socket.
      //Création d'un objet, utilisation de la méthode constructeur
      else if(line.contains(" new ")){
        writers[nClient].write("    dos.writeUTF(\"constructeur\");\n");
        //Lecture de l'argument et du nom (on suppose un seul argument)
        int parentheseOuvrante = line.indexOf('(');
        int parentheseFermante = line.indexOf(')');
        int egal = line.indexOf("=");
        int espace = line.indexOf(" ");
        for (int i =  0; line.indexOf(" ",i) < egal-1; i++){
          espace = line.indexOf(" ", i);
        }
        String argu = line.substring(parentheseOuvrante+1,parentheseFermante);
        writers[nClient].write("    dos.write");
        //Que faut-il envoyer ? la sortie dépend de l'argument.
        if (methodsArg.get(0).equals("int")) writers[nClient].write("Int(");
        else if (methodsArg.get(0).equals("float")) writers[nClient].write("Float(");
        else if (methodsArg.get(0).equals("char")) writers[nClient].write("Char(");
        else writers[0].write("Byte(");
        //On ajoute l'argument.
        writers[nClient].write(argu + ");\n");
      }
      //Utilisation de toute autre méthode :
      else if (line.contains(".")) {
        boolean hasBeenWritten = false; //Nous permet de vérifier si la ligne a pu être réécrite
        for (int j = 1; j < methods.size(); j++) {
          if (line.contains(methods.get(j))) {
            //Lecture de l'argument et du nom de la méthode (on suppose un seul argument)
            int parentheseOuvrante = line.indexOf('(');
            int parentheseFermante = line.indexOf(')');
            int egal = line.indexOf("=");
            String argu = line.substring(parentheseOuvrante + 1, parentheseFermante);
            writers[nClient].write("    dos.writeUTF(\"" + methods.get(j) + "\");\n");
            writers[nClient].write("    dos.write");
            //Que faut-il envoyer ? la sortie dépend de l'argument.
            if (methodsArg.get(j).equals("int")) writers[nClient].write("Int(");
            else if (methodsArg.get(j).equals("float")) writers[nClient].write("Float(");
            else if (methodsArg.get(j).equals("char")) writers[nClient].write("Char(");
            else writers[nClient].write("Byte(");
            //On ajoute l'argument.
            writers[nClient].write(argu + ");\n");
            hasBeenWritten = true;
            //On récupère, enfin, le nom de la variable résultat.
            int espace = line.indexOf(" ");
            for (int i =  0; line.indexOf(" ",i) < egal-1; i++){
              espace = line.indexOf(" ", i);
            }
            String nomResultat = line.substring(espace+1,egal-1);
            res = nomResultat;
          }
        }
        if (!hasBeenWritten) writers[nClient].write(line + "\n");
      }
      else writers[nClient].write(line + "\n");
    }


    //Code des autres classes. Il n'y a pas grand chose à changer, à part implémenter Serializable
    if(nClient>1){
      for(int i=1; i<nClient; i++){
        while ((line = readers[i].readLine()) != null) {
          if (line.contains("package")){
              int pointVirgule;
              pointVirgule = line.indexOf(";");
              writers[i].write(line.substring(0,pointVirgule) + ".v2;\n");
          }
          else if (line.contains("class "+methods.get(i))){
            int acolladeOuvrante = line.indexOf('{');
            writers[i].write(line.substring(0,acolladeOuvrante));
            writers[i].write("implements Serializable {\n");
          }
          else writers[i].write(line+"\n");
        }
      }
    }

    //TODO : traitement des erreurs.


    //Fermeture des writers :

    for(int i=0; i<writers.length; i++) writers[i].close();
    bwIfc.close();

 }
}
