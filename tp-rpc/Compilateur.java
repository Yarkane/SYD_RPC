//package rpc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Compilateur {
  public static void main(String [] arg) throws IOException {
    //Gestion des arguments :
    if (arg.length != 2) {
      System.out.println("Bad usage : Compilateur <nom_du_code_java> <port>");
    }
    String filename = arg[0];
    String port = arg[1];

    //Création du reader
    FileReader reader = new FileReader(filename);
    BufferedReader br = new BufferedReader(reader);

    //Création du Writer
    String outputFile = "RPC_" + filename;
    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

    // read line by line
    String line;
    String nomObjet = "jfkregkjfklgrm flelkghergkt hk";
    String nomResultat = "jfkregkjfklgrm flelkghergkt hk";
    while ((line = br.readLine()) != null) {
      //détection du main
      if(line.contains("public static void main(")){
        //Le fichier est un CLIENT
        //Première étape : ouvrir socket
        String clientStart =  line + "\n    java.net.Socket s = new java.net.Socket(\"localhost\", " + port + ");\n    java.io.DataOutputStream dos = new java.io.DataOutputStream(s.getOutputStream());\n    java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());\n";
        writer.write(clientStart);

        while ((line = br.readLine()) != null) {
          //Si constructeur (on suppose un seul objet)
          if(line.contains("= new ")){
            writer.write("    dos.writeUTF(\"constructeur\");\n");
            //Lecture de l'argument et du nom (on suppose un seul argument)
            int parenthèseOuvrante = line.indexOf('(');
            int parenthèseFermante = line.indexOf(')');
            int egal = line.indexOf("=");
            int espace = line.indexOf(" ");
            for (int i =  0; line.indexOf(" ",i) < egal-1; i++){
               espace = line.indexOf(" ", i);
            }
            nomObjet = line.substring(espace+1,egal-1);
            String argu = line.substring(parenthèseOuvrante+1,parenthèseFermante);
            writer.write("    dos.writeInt(" + argu + ");\n");

          }
          //Si méthode :
          else if (line.contains(" " + nomObjet +".")) {
            //Lecture de l'argument et du nom de la méthode (on suppose un seul argument)
            int parenthèseOuvrante = line.indexOf('(');
            int parenthèseFermante = line.indexOf(')');
            int point = line.indexOf(".");
            int egal = line.indexOf("=");
            String methode = line.substring(point+1,parenthèseOuvrante);
            String argu = line.substring(parenthèseOuvrante+1,parenthèseFermante);
            writer.write("    dos.writeUTF(\""+ methode +"\");\n");
            writer.write("    dos.writeInt("+ argu +");\n");
            //On récupère, enfin, le nom de la variable résultat.
            int espace = line.indexOf(" ");
            for (int i =  0; line.indexOf(" ",i) < egal-1; i++){
               espace = line.indexOf(" ", i);
            }
            nomResultat = line.substring(espace+1,egal-1);
          }
          //Si besoin de lire les sorties du serveur
          else if (line.contains(nomResultat)) writer.write(line.replace(nomResultat,"ois.readObject()") + "\n");
          //Tous les autres cas dans le cas du Client
          else writer.write(line + "\n");
        }


      }


      else writer.write(line + "\n");
    }
    writer.close();
  }
}
