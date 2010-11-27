package project;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class SwingView implements View {
  private Mancala mancala;
  private GameForm gameForm = new GameForm();
  private Collection<? extends Player> players = new ArrayList<Player>();
  private NewGameAction newGameAction = new NewGameAction("New Game", players);
  private JFrame gameFrame = new JFrame("Mancala");
  private JMenuBar menuBar = new JMenuBar();
  private ReseedAction[][] reseedActions;
  private ContainerListener[] containerListeners;

  public SwingView() {
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newGame = new JMenuItem();
    newGame.setAction(newGameAction);
    gameMenu.add(newGame);
    gameMenu.add(new JMenuItem("History"));
    gameMenu.addSeparator();
    gameMenu.add(new JMenuItem("Exit"));
    menuBar.add(gameMenu);

    gameFrame.getContentPane().add(gameForm.$$$getRootComponent$$$());
    gameFrame.setJMenuBar(menuBar);
    gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    gameFrame.setLocationRelativeTo(null);
    gameFrame.pack();
    gameFrame.setResizable(false);

    reseedActions = new ReseedAction[2][6];
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 6; j++) {
        reseedActions[i][j] = new ReseedAction();
      }
    }

    gameForm.getA1().setAction(reseedActions[0][0]);
    gameForm.getA2().setAction(reseedActions[0][1]);
    gameForm.getA3().setAction(reseedActions[0][2]);
    gameForm.getA4().setAction(reseedActions[0][3]);
    gameForm.getA5().setAction(reseedActions[0][4]);
    gameForm.getA6().setAction(reseedActions[0][5]);

    gameForm.getB1().setAction(reseedActions[1][0]);
    gameForm.getB2().setAction(reseedActions[1][1]);
    gameForm.getB3().setAction(reseedActions[1][2]);
    gameForm.getB4().setAction(reseedActions[1][3]);
    gameForm.getB5().setAction(reseedActions[1][4]);
    gameForm.getB6().setAction(reseedActions[1][5]);
  }

  public void start() {
    gameFrame.setVisible(true);
  }

  public void afterInit() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void afterReseed(int captured) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void beforeReseed() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public Mancala getMancala() {
    return mancala;
  }

  public boolean setMancala(Mancala value) {
    if (value == mancala) {
      return false;
    }
    Mancala oldValue = mancala;
    if (oldValue != null) {
      mancala = null;
      oldValue.removeFromViews(this);
    }
    mancala = value;
    if (value != null) {
      value.addToViews(this);
    }
    mancalaChanged();
    return true;
  }

  private void mancalaChanged() {
    newGameAction.setMancala(mancala);
  }

  public void removeYou() {
    setMancala(null);
  }

  public Iterable<? extends Player> getNewGamePlayers() {
    return null;  //To change body of created methods use File | Settings | File Templates.
  }

  public void updateContainer(Container container) {

  }
}
