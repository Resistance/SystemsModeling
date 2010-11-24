package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Map;

public class NewGameAction extends AbstractAction {
  private final Mancala mancala;
  private Iterable<Player> players;
  private Map<Player, JButton> storages;
  private Map<Player, Iterable<JButton>> pits;

  public NewGameAction(String name, Mancala mancala) {
    super(name);
    this.mancala = mancala;
  }

  public void actionPerformed(ActionEvent e) {
    mancala.removeAllFromPlayers();
    for (Player player : players) {
      mancala.addToPlayers(player);
    }
    mancala.init();
    for (Player player : players) {
      ContainerListener storageListener = new ContainerListener(player.getStorage(), storages.get(player));
      storageListener.register();
      storageListener.update();
      Iterator<JButton> pitButtons = pits.get(player).iterator();
      for (Pit pit : player.getPits()) {
        JButton pitButton = pitButtons.next();
        pitButton.setAction(new ReseedAction(mancala, pit));
        ContainerListener pitListener = new ContainerListener(pit, pitButton);
        pitListener.register();
        pitListener.update();
      }
    }
    mancala.setCurrentPlayer(players.iterator().next());
  }

  public void setPlayers(Iterable<Player> players) {
    this.players = players;
  }

  public void setStorages(Map<Player, JButton> storages) {
    this.storages = storages;
  }

  public void setPits(Map<Player, Iterable<JButton>> pits) {
    this.pits = pits;
  }
}
