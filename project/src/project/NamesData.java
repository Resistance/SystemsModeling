package project;

public class NamesData {
  private String name1;
  private String name2;
  private int seeds;
  private boolean ok;

  public NamesData() {
  }

  public String getName1() {
    return name1;
  }

  public void setName1(final String name1) {
    this.name1 = name1;
  }

  public String getName2() {
    return name2;
  }

  public void setName2(final String name2) {
    this.name2 = name2;
  }

  public int getSeeds() {
    return seeds;
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
