package project;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ContainerListener implements PropertyChangeListener {
  private SwingView view;
  private Container container;

  public ContainerListener() {
  }

  public void register() {
    container.addPropertyChangeListener(Container.PROPERTY_SEEDS, this);
  }

  public void unregister() {
    container.removePropertyChangeListener(Container.PROPERTY_SEEDS, this);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    view.updateContainer(container);
  }

  public SwingView getView() {
    return view;
  }

  public void setView(SwingView view) {
    this.view = view;
  }

  public Container getContainer() {
    return container;
  }

  public void setContainer(Container container) {
    this.container = container;
  }
}
