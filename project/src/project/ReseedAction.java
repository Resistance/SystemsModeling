package project;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ReseedAction extends AbstractAction {
  private Mancala mancala;
  private Pit pit;

  public void actionPerformed(ActionEvent e) {
    mancala.reseed(pit);
  }

  public Mancala getMancala() {
    return mancala;
  }

  public void setMancala(Mancala mancala) {
    this.mancala = mancala;
  }

  public Pit getPit() {
    return pit;
  }

  public void setPit(Pit pit) {
    this.pit = pit;
  }
}
