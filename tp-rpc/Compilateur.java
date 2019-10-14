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
    while ((line = br.readLine()) != null) {
      //détection du main
      if(line.contains("public static void main(")){
        //Le fichier est un CLIENT
        //Première étape : ouvrir socket
        String clientStart =  line + "\n          java.net.Socket s = new java.net.Socket('localhost', " + port + ");\n          java.io.DataOutputStream dos = new java.io.DataOutputStream(s.getOutputStream());\n          java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());\n";
        writer.write(clientStart);

        while ((line = br.readLine()) != null) {
          //Si constructeur (on suppose un seul objet)
          if(line.contains("= new ")){
            writer.write("dos.writeUTF('constructeur');\n");
            //Lecture de l'argument (on suppose un seul argument)
            int parenthèseOuvrante = line.indexOf('(');
            int parenthèseFermante = line.indexOf(')');
            String argu = line.substring(parenthèseOuvrante,parenthèseFermante);
            writer.write("dos.writeInt(" + argu + ");\n");
          }
          //Si méthode :



        }


      }



    }
    writer.close();
  }
}
