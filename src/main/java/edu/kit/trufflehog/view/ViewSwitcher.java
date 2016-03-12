package edu.kit.trufflehog.view;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ViewSwitcher {
    private final Map<String, AnchorPane> views;

    public ViewSwitcher() {
        views = new HashMap<>();
    }

    public void putView(String name, AnchorPane anchorPane) {
        views.put(name, anchorPane);
    }

    public void removeView(String name) {
        views.remove(name);
    }

    public boolean replace(final AnchorPane currentView, String name) {
        if (views.containsKey(name)) {
            SplitPane splitPane = (SplitPane) currentView.getParent().getParent();

            splitPane.getItems().remove(currentView);
            splitPane.getItems().add(views.get(name));
            return true;
        }

        return false;
    }
}
