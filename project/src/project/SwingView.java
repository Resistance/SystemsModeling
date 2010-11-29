package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

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
  private Player currentPlayer;
  private int currentPlayerScore;
  private int otherPlayerScore;
  private int currentPlayerIndex;

  public SwingView() {
    JMenu gameMenu = new JMenu("Game");
    JMenuItem newGame = new JMenuItem();
    newGame.setAction(newGameAction);
    gameMenu.add(newGame);
    JMenuItem history = new JMenuItem("History");
    gameMenu.add(history);
    gameMenu.addSeparator();
    JMenuItem exitGame = new JMenuItem("Exit");
    gameMenu.add(exitGame);
    menuBar.add(gameMenu);

    gameFrame.getContentPane().add(gameForm.$$$getRootComponent$$$());
    gameFrame.setJMenuBar(menuBar);
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameFrame.pack();
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension screenSize = tk.getScreenSize();
    gameFrame.setLocation( (screenSize.width - gameFrame.getWidth())/2, (screenSize.height - gameFrame.getHeight())/2 );
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

    history.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new HistoryForm().setVisible(true);
      }
    });

    exitGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gameFrame.dispose();
      }
    });
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
    JLabel[] nameLabels = {gameForm.getRight(), gameForm.getLeft()};
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
      nameLabels[i].setText(player.getName());
      i++;
    }
    updateReseedActionWantEnabled();
    gameForm.getStatus().setText(mancala.getCurrentPlayer().getName() + " starts this game.");
  }

  public void beforeReseed() {
    currentPlayer = mancala.getCurrentPlayer();
    currentPlayerScore = currentPlayer.getScore();
    otherPlayerScore = currentPlayer.getNext().getScore();
  }

  public void afterReseed(int captured) {
    updateReseedActionWantEnabled();

    int currentPlayerNewScore = currentPlayer.getScore();
    int currentPlayerScoreDiff = currentPlayerNewScore - currentPlayerScore;

    StringBuilder sb = new StringBuilder();
    StringBuilder gameOver = new StringBuilder();

    if (mancala.isOver()) {
      sb.append("Game over.");
      Player winner = mancala.getPreviousWinner();
      if (winner != null) {
        gameOver.append("'");
        gameOver.append(winner.getName());
        gameOver.append("' won this game ");
        gameOver.append(winner.getScore());
        gameOver.append(":");
        gameOver.append(winner.getNext().getScore());
        gameOver.append(" over '");
        gameOver.append(winner.getNext().getName());
        gameOver.append("'. Well done!");
      }
      else {
        sb.append("Draw game.");
        gameOver.append("Draw game, well played!");
      }
    }
    else {
      if (currentPlayerScoreDiff > 0) {
        sb.append(currentPlayer.getName());
        sb.append(" scored ");
        sb.append(currentPlayerScoreDiff);
        Player newCurrentPlayer = mancala.getCurrentPlayer();
        if (newCurrentPlayer == currentPlayer)
          sb.append(" and gets another turn.");
        else
          sb.append(".");
      }
    }
    gameForm.getStatus().setText(sb.toString());
    if (mancala.isOver()) {
      new GameOverDialog(gameOver.toString(), mancala).setVisible(true);
    }
  }

  private void updateReseedActionWantEnabled() {
    boolean over = mancala.isOver();
    int currentPlayerIndex = mancala.indexOfPlayers(mancala.getCurrentPlayer());
    ListIterator<ReseedAction> it = reseedActions.listIterator();
    while (it.hasNext()) {
      int i = it.nextIndex();
      it.next().setWantEnabled(!over && (i / PITS_PER_PLAYER == currentPlayerIndex));
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