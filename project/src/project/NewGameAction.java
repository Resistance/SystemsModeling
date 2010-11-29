package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class NewGameAction extends AbstractAction {
  private Mancala mancala;

  public NewGameAction(String name, Mancala mancala) {
    super(name);
    this.mancala = mancala;
  }

  public NewGameAction(String name) {
    super(name);
  }

  public void actionPerformed(ActionEvent e) {
    NamesData data = new NamesData();
    List<? extends Player> players = mancala.getPlayers();
    if (players.size() == 2) {
      data.setRight(players.get(0).getName());
      data.setLeft(players.get(1).getName());
      data.setSeeds(mancala.getSeedsPerPit());
    }
    NamesForm namesForm = new NamesForm();
    namesForm.setData(data);
    namesForm.setVisible(true);
    namesForm.getData(data);
    if (data.isOk()) {
      mancala.removeAllFromPlayers();
      mancala.addToPlayers(new Player().withName(data.getRight()));
      mancala.addToPlayers(new Player().withName(data.getLeft()));
      mancala.init(data.getSeeds());
    }
  }

  public Mancala getMancala() {
    return mancala;
  }

  public void setMancala(Mancala mancala) {
    this.mancala = mancala;
    invalidate();
  }

  private void invalidate() {
    setEnabled(mancala != null);
  }
}
