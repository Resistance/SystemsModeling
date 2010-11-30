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

  // start a new Game
  public void actionPerformed(ActionEvent e) {
    NamesData data = new NamesData();
    List<? extends Player> players = mancala.getPlayers();

    // ask players' names and seeds-per-pit from the Mancala and display them in the NamesForm
    if (players.size() == 2) {
      data.setRight(players.get(0).getName());
      data.setLeft(players.get(1).getName());
      data.setSeeds(mancala.getSeedsPerPit());
    }
    NamesForm namesForm = new NamesForm();
    namesForm.setData(data);
    namesForm.setVisible(true);
    namesForm.getData(data);

    // get the new players' names and seeds-per-pit from the NamesForm and set them to the Mancala
    if (data.isOk()) {
      mancala.removeAllFromPlayers();
      mancala.addToPlayers(new Player().withName(data.getRight()));
      mancala.addToPlayers(new Player().withName(data.getLeft()));
      mancala.setSeedsPerPit(data.getSeeds());
      mancala.init();
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
