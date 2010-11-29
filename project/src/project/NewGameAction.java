package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;

public class NewGameAction extends AbstractAction {
  private Mancala mancala;

  public NewGameAction(String name) {
    super(name);
  }

  public void actionPerformed(ActionEvent e) {
    NamesData data = new NamesData();
    List<? extends Player> players = mancala.getPlayers();
    if (players.size() == 2) {
      data.setName1(players.get(0).getName());
      data.setName2(players.get(1).getName());
    }
    NamesForm namesForm = new NamesForm();
    namesForm.setData(data);
    namesForm.setVisible(true);
    namesForm.getData(data);
    if (data.isOk()) {
      mancala.removeAllFromPlayers();
      mancala.addToPlayers(new Player().withName(data.getName1()));
      mancala.addToPlayers(new Player().withName(data.getName2()));
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
