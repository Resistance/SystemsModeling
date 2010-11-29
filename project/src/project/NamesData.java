package project;

public class NamesData {
  private String left;
  private String right;
  private int seeds;
  private boolean ok;

  public NamesData() {
  }

  public String getLeft() {
    if (left != null && left.length() > 0) return left;
    return "Left Player";
  }

  public void setLeft(final String leftPlayer) {
    this.left = leftPlayer;
  }

  public String getRight() {
    if (right != null && right.length() > 0) return right;
    return "Right Player";
  }

  public void setRight(final String rightPlayer) {
    this.right = rightPlayer;
  }

  public int getSeeds() {
    if (seeds != 0) return seeds;
    return 4;
  }

  public void setSeeds(final int seeds) {
    this.seeds = seeds;
  }

  public void setOk(boolean ok) {
    this.ok = ok;
  }

  public boolean isOk() {
    return ok;
  }
}
