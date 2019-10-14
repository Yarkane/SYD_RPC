  public static void main(String [] arg) {
          java.net.Socket s = new java.net.Socket('localhost', 8080);
          java.io.DataOutputStream dos = new java.io.DataOutputStream(s.getOutputStream());
          java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());
dos.writeUTF('constructeur');
dos.writeInt((10);
