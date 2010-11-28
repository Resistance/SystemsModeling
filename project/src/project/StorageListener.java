package project;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StorageListener implements PropertyChangeListener {
  private Storage storage;
  private JButton button;
  private static final String DEFAULT_SEEDS = "0";

  public StorageListener() {
  }

  public StorageListener(JButton button) {
    this.button = button;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    invalidate();
  }

  public Container getStorage() {
    return storage;
  }

  public void setStorage(Storage storage) {
    if (storage != this.storage) {
      if (this.storage != null) {
        this.storage.removePropertyChangeListener(Container.PROPERTY_SEEDS, this);
      }
      this.storage = storage;
      if (storage != null) {
        storage.addPropertyChangeListener(Container.PROPERTY_SEEDS, this);
      }
      invalidate();
    }
  }

  private void invalidate() {
    if (button != null) {
      button.setText(storage != null ? String.valueOf(storage.getSeeds()) : DEFAULT_SEEDS);
    }
  }

  public JButton getButton() {
    return button;
  }

  public void setButton(JButton button) {
    if (button != this.button) {
      this.button = button;
      invalidate();
    }
  }
}
