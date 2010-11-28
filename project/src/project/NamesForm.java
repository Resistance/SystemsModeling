package project;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NamesForm extends JDialog {
  private JPanel panel1;
  private JTextField pleaseEnterTheNameTextField;
  private JTextField pleaseEnterTheNameTextField1;
  private JButton startNewGameButton;
  private JButton cancelButton;
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
    setLocationRelativeTo(null);
    pack();
    setResizable(false);
  }

  public void setData(NamesData data) {
    pleaseEnterTheNameTextField.setText(data.getName1());
    pleaseEnterTheNameTextField1.setText(data.getName2());
  }

  public void getData(NamesData data) {
    data.setName1(pleaseEnterTheNameTextField.getText());
    data.setName2(pleaseEnterTheNameTextField1.getText());
    data.setOk(ok);
  }

  public boolean isModified(NamesData data) {
    if (pleaseEnterTheNameTextField.getText() != null ? !pleaseEnterTheNameTextField.getText().equals(data.getName1()) : data.getName1() != null)
      return true;
    if (pleaseEnterTheNameTextField1.getText() != null ? !pleaseEnterTheNameTextField1.getText().equals(data.getName2()) : data.getName2() != null)
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
    label1.setText("Please enter the name of the first player:");
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
    pleaseEnterTheNameTextField = new JTextField();
    pleaseEnterTheNameTextField.setColumns(20);
    pleaseEnterTheNameTextField.setFocusCycleRoot(true);
    pleaseEnterTheNameTextField.setFocusTraversalPolicyProvider(false);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel2.add(pleaseEnterTheNameTextField, gbc);
    final JLabel label2 = new JLabel();
    label2.setFocusable(false);
    label2.setText("Please enter the name of the second player:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel2.add(label2, gbc);
    pleaseEnterTheNameTextField1 = new JTextField();
    pleaseEnterTheNameTextField1.setColumns(30);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel2.add(pleaseEnterTheNameTextField1, gbc);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.fill = GridBagConstraints.BOTH;
    panel2.add(panel3, gbc);
    cancelButton = new JButton();
    cancelButton.setText("Cancel");
    panel3.add(cancelButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer2 = new Spacer();
    panel3.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    startNewGameButton = new JButton();
    startNewGameButton.setText("Start new game");
    panel3.add(startNewGameButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    label1.setLabelFor(pleaseEnterTheNameTextField);
    label2.setLabelFor(pleaseEnterTheNameTextField1);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return panel1;
  }
}
