package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton oneMoreRoundButton;
  private JLabel message;

  public GameOverDialog(String message, Mancala mancala) {
    this.message.setText(message);
    NewGameAction newGame = new NewGameAction("New Game", mancala);
    oneMoreRoundButton.setAction(newGame);

    setTitle("Game Over");
    setContentPane(contentPane);
    pack();
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension screenSize = tk.getScreenSize();
    setLocation( (screenSize.width - getWidth())/2, (screenSize.height - getHeight())/2 );
    setResizable(false);
    setModal(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onOK();
      }
    });

    oneMoreRoundButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        oneMore();
      }
    });

// call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onOK();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOK() {
// add your code here
    dispose();
  }

  private void oneMore() {
// add your code here if necessary

    dispose();
  }
}
