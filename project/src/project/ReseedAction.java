package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ReseedAction extends AbstractAction implements PropertyChangeListener {
  private Mancala mancala;
  private Pit pit;
  boolean wantEnabled = enabled;
  private static final String DEFAULT_SEEDS = "4";

  public void actionPerformed(ActionEvent e) {
    mancala.reseed(pit);
  }

  public Mancala getMancala() {
    return mancala;
  }

  public void setMancala(Mancala mancala) {
    if (mancala != this.mancala) {
      if (this.mancala != null) {
        //this.mancala.remove
      }
      this.mancala = mancala;
      invalidate();
    }
  }

  private void invalidate() {
    updateEnabled();
    putValue(Action.NAME, pit != null ? String.valueOf(pit.getSeeds()) : DEFAULT_SEEDS);
  }

  private void updateEnabled() {
    setEnabled((mancala != null) && (pit != null) && wantEnabled);
  }

  public Pit getPit() {
    return pit;
  }

  public void setPit(Pit pit) {
    if (pit != this.pit) {
      if (this.pit != null) {
        this.pit.removePropertyChangeListener(Container.PROPERTY_SEEDS, this);
      }
      this.pit = pit;
      if (pit != null) {
        pit.addPropertyChangeListener(Container.PROPERTY_SEEDS, this);
      }
      invalidate();
    }
  }

  public void propertyChange(PropertyChangeEvent evt) {
    putValue(Action.NAME, String.valueOf(pit.getSeeds()));
  }

  public boolean isWantEnabled() {
    return wantEnabled;
  }

  public void setWantEnabled(boolean wantEnabled) {
    if (wantEnabled != this.wantEnabled) {
      this.wantEnabled = wantEnabled;
      updateEnabled();
    }
  }
}
