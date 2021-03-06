package project;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class NamesForm extends JDialog {
  private JPanel panel1;
  private JTextField leftPlayerTextField;
  private JTextField rightPlayerTextField;
  private JButton startNewGameButton;
  private JButton cancelButton;
  private JTextField seedsField;
  private Window window;
  private boolean ok;

  public NamesForm() {
    setTitle("Player Names");
    setContentPane($$$getRootComponent$$$());
    setModal(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    startNewGameButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ok = true;
        dispose();
      }
    });
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    $$$getRootComponent$$$().registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    getRootPane().setDefaultButton(startNewGameButton);
    pack();
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension screenSize = tk.getScreenSize();
    setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
    setResizable(false);
  }

  public void setData(NamesData data) {
    leftPlayerTextField.setText(data.getLeft());
    rightPlayerTextField.setText(data.getRight());
    seedsField.setText(String.valueOf(data.getSeeds()));
  }

  public void getData(NamesData data) {
    data.setLeft(leftPlayerTextField.getText());
    data.setRight(rightPlayerTextField.getText());
    try {
      data.setSeeds(Integer.parseInt(seedsField.getText()));
    }
    catch (NumberFormatException ignored) {
    }
    data.setOk(ok);
  }

  public boolean isModified(NamesData data) {
    if (leftPlayerTextField.getText() != null ? !leftPlayerTextField.getText().equals(data.getLeft()) : data.getLeft() != null)
      return true;
    if (rightPlayerTextField.getText() != null ? !rightPlayerTextField.getText().equals(data.getRight()) : data.getRight() != null)
      return true;
    return false;
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    panel1 = new JPanel();
    panel1.setLayout(new BorderLayout(0, 0));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridBagLayout());
    panel2.setAlignmentX(0.5f);
    panel2.setOpaque(true);
    panel2.setVisible(true);
    panel1.add(panel2, BorderLayout.CENTER);
    panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), null));
    final JLabel label1 = new JLabel();
    label1.setFocusCycleRoot(false);
    label1.setFocusable(false);
    label1.setText("Left player:");
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    panel2.add(label1, gbc);
    final JPanel spacer1 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel2.add(spacer1, gbc);
    leftPlayerTextField = new JTextField();
    leftPlayerTextField.setColumns(20);
    leftPlayerTextField.setFocusCycleRoot(false);
    leftPlayerTextField.setFocusTraversalPolicyProvider(false);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel2.add(leftPlayerTextField, gbc);
    final JLabel label2 = new JLabel();
    label2.setFocusable(false);
    label2.setText("Right player:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel2.add(label2, gbc);
    rightPlayerTextField = new JTextField();
    rightPlayerTextField.setColumns(30);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel2.add(rightPlayerTextField, gbc);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.fill = GridBagConstraints.BOTH;
    panel2.add(panel3, gbc);
    cancelButton = new JButton();
    cancelButton.setText("Cancel");
    panel3.add(cancelButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer2 = new Spacer();
    panel3.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    startNewGameButton = new JButton();
    startNewGameButton.setSelected(true);
    startNewGameButton.setText("Start game");
    panel3.add(startNewGameButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    seedsField = new JTextField();
    seedsField.setAlignmentX(0.5f);
    seedsField.setColumns(3);
    seedsField.setText("4");
    panel4.add(seedsField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, -1), null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setText("Seeds per pit:");
    panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    label1.setLabelFor(leftPlayerTextField);
    label2.setLabelFor(rightPlayerTextField);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return panel1;
  }
}
