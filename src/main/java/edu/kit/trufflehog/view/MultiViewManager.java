package edu.kit.trufflehog.view;

import edu.kit.trufflehog.view.controllers.AnchorPaneInteractionController;
import javafx.geometry.Orientation;
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
public class MultiViewManager {
    // Views
    public static final String START_VIEW = "Start";
    public static final String DEMO_VIEW = "Demo";
    public static final String PROFINET_VIEW = "Profinet";
    public static final String CAPTURE_VIEW = "capture";

    // Other variables
    private final Map<String, AnchorPaneInteractionController> views;
    private final Map<String, Boolean> used;
    private AnchorPane selected;
    private int viewCounter = 1;

    /**
     * <p>
     *
     * </p>
     */
    public MultiViewManager() {
        this.selected  = null;
        views = new HashMap<>();
        used = new HashMap<>();
    }

    /**
     * <p>
     *
     * </p>
     *
     * @param name
     * @param anchorPane
     */
    public void putView(final String name, final AnchorPaneInteractionController anchorPane) {
        if (name != null && anchorPane != null) {
            views.put(name, anchorPane);
            used.put(name, false);
        }
    }

    /**
     * <p>
     *
     * </p>
     *
     * @param name
     */
    public void removeView(String name) {
        if (name != null) {
            views.remove(name);
        }
    }

    public void splitHorizontal() {
        SplitPane splitPane = (SplitPane) selected.getParent().getParent();

        if (splitPane.getOrientation().equals(Orientation.HORIZONTAL)) {
            StartViewController startViewController = (StartViewController) views.get(START_VIEW);
            StartViewController clone = startViewController.clone();
            clone.showCloseButton(true);
            splitPane.getItems().add(clone);
        } else {
            StartViewController startViewController = (StartViewController) views.get(START_VIEW);
            StartViewController clone = startViewController.clone();
            clone.showCloseButton(true);
            SplitPane horizontalSplitPane = new SplitPane(clone);
            splitPane.getItems().add(horizontalSplitPane);
        }

        viewCounter++;
    }

    public void splitVertical() {
        viewCounter++;
    }

    public void merge() {

    }

    public boolean replace(final AnchorPane currentView, final String name) {
        if (views.containsKey(name)) {
            SplitPane splitPane = (SplitPane) currentView.getParent().getParent();

            splitPane.getItems().replaceAll(view -> {
                if (view.equals(currentView)) {
                    return views.get(name).clone();
                }
                return view;
            });
            //splitPane.getItems().add(views.get(name));
            return true;
        }

        return false;
    }

    public void setSelected(AnchorPane anchorPane) {
        selected = anchorPane;
    }
}
