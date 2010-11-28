package project;

import javax.swing.*;
import java.util.*;

public class SwingView implements View {
  private static final int NUM_PLAYERS = 2;
  private static final int PITS_PER_PLAYER = 6;
  private Mancala mancala;
  private GameForm gameForm = new GameForm();
  private NewGameAction newGameAction = new NewGameAction("New Game");
  private JFrame gameFrame = new JFrame("Mancala");
  private JMenuBar menuBar = new JMenuBar();
  private List<ReseedAction> reseedActions = new ArrayList<ReseedAction>(NUM_PLAYERS * PITS_PER_PLAYER);
  private List<StorageListener> storageListeners = new ArrayList<StorageListener>(NUM_PLAYERS);

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

    JButton[] pitButtons = {
        gameForm.getA1(), gameForm.getA2(), gameForm.getA3(), gameForm.getA4(), gameForm.getA5(), gameForm.getA6(),
        gameForm.getB1(), gameForm.getB2(), gameForm.getB3(), gameForm.getB4(), gameForm.getB5(), gameForm.getB6()
    };
    for (int i = 0; i < NUM_PLAYERS * PITS_PER_PLAYER; i++) {
      ReseedAction reseedAction = new ReseedAction();
      reseedActions.add(reseedAction);
      pitButtons[i].setAction(reseedAction);
    }

    storageListeners.add(new StorageListener(gameForm.getA0()));
    storageListeners.add(new StorageListener(gameForm.getB0()));
  }

  public void start() {
    gameFrame.setVisible(true);
  }

  public void afterInit() {
    List<? extends Player> players = mancala.getPlayers();
    int playersSize = players.size();
    if (playersSize != NUM_PLAYERS) {
      throw new IllegalStateException("The game has " + playersSize + " players, but this view requires exactly " + NUM_PLAYERS);
    }
    int i = 0;
    for (Player player : players) {
      List<? extends Pit> pits = player.getPits();
      int pitsSize = pits.size();
      if (pitsSize != PITS_PER_PLAYER) {
        throw new IllegalStateException("The player + " + player.getName() + " has " + pitsSize + " pits, but this view requires exactly " + PITS_PER_PLAYER);
      }
      int j = 0;
      for (Pit pit : pits) {
        reseedActions.get(i * PITS_PER_PLAYER + j).setPit(pit);
        j++;
      }
      storageListeners.get(i).setStorage(player.getStorage());
      i++;
    }
    updateReseedActionWantEnabled();    
  }

  public void beforeReseed() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void afterReseed(int captured) {
    updateReseedActionWantEnabled();
  }

  private void updateReseedActionWantEnabled() {
    int currentPlayerIndex = mancala.indexOfPlayers(mancala.getCurrentPlayer());
    ListIterator<ReseedAction> it = reseedActions.listIterator();
    while (it.hasNext()) {
      int i = it.nextIndex();
      it.next().setWantEnabled(i / PITS_PER_PLAYER == currentPlayerIndex);
    }
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
    for (ReseedAction reseedAction : reseedActions) {
      reseedAction.setMancala(mancala);
    }
  }

  public void removeYou() {
    setMancala(null);
  }

  public static void main(String[] args) {
    SwingView view = new SwingView();
    Mancala mancala = new Mancala();
    view.setMancala(mancala);
    view.start();
  }
}
