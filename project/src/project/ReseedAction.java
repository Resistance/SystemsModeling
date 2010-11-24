package project;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ReseedAction extends AbstractAction {
  private final Mancala mancala;
  private final Pit pit;

  public ReseedAction(Mancala mancala, Pit pit) {
    this.mancala = mancala;
    this.pit = pit;
  }

  public void actionPerformed(ActionEvent e) {
    mancala.reseed(pit);
  }
}
