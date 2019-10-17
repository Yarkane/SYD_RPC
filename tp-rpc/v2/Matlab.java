package v2;
public class Matlab implements MatlabIfc {
  public static void main(String [] arg) throws java.io.IOException {
    Matlab m = null;
    java.net.ServerSocket sos = new java.net.ServerSocket(8080);
    java.net.Socket s = sos.accept();
    java.io.DataInputStream dis = new java.io.DataInputStream(s.getInputStream());
    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());
    String fonction = dis.readUTF();
        if (fonction.equals("constructeur")) {
      m = new Matlab(dis.readInt());
    }
        if (fonction.equals("calcul")) {
      oos.writeObject(m.calcul(dis.readInt()));
    }
  }
  private int i;

  public Matlab(int i) {
    this.i = i;
  }

  public Result calcul(int in) {
    return new Result(in * this.i);
  }
}
