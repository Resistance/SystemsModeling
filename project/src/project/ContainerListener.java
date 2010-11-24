package project;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ContainerListener implements PropertyChangeListener {
  private final JButton button;
  private final Container container;

  public ContainerListener(Container container, JButton button) {
    this.button = button;
    this.container = container;
  }

  public void register() {
    container.addPropertyChangeListener(Container.PROPERTY_SEEDS, this);
  }

  public void unregister() {
    container.removePropertyChangeListener(Container.PROPERTY_SEEDS, this);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    update();
  }

  public void update() {
    button.setText(String.valueOf(container.getSeeds()));
  }
}
