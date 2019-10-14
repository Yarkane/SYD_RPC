package rpc;
public class Matlab implements MatlabIfc {
  private int i;

  public Matlab(int i) {
    this.i = i;
  }

  public Result calcul(int in) {
    return new Result(in * this.i);
  }
}
