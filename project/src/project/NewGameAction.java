package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Map;

public class NewGameAction extends AbstractAction {
  private Mancala mancala;
  private Iterable<? extends Player> players;

  public NewGameAction(String name) {
    super(name);
  }

  public NewGameAction(String name, Iterable<? extends Player> players) {
    this(name);
    this.players = players;
  }

  public void actionPerformed(ActionEvent e) {
    mancala.removeAllFromPlayers();
    for (Player player : players) {
      mancala.addToPlayers(player);
    }
    mancala.init();
  }

  public Mancala getMancala() {
    return mancala;
  }

  public void setMancala(Mancala mancala) {
    this.mancala = mancala;
  }

  public Iterable<? extends Player> getPlayers() {
    return players;
  }

  public void setPlayers(Iterable<? extends Player> players) {
    this.players = players;
  }
}
