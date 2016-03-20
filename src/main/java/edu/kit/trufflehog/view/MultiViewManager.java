package edu.kit.trufflehog.view;

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
    private final Map<String, SplitableView> views;
    private SplitableView selected;
    private int viewCounter = 1;

    /**
     * <p>
     *
     * </p>
     */
    public MultiViewManager() {
        this.selected  = null;
        views = new HashMap<>();
    }

    /**
     * <p>
     *
     * </p>
     *
     * @param name
     * @param splitableView
     */
    public void putView(final String name, final SplitableView splitableView) {
        if (name != null && splitableView != null) {
            views.put(name, splitableView);
        }
    }

    public void split(Orientation orientation) {
        // Make sure that the orientation is valid and that a view is selected
        if ((!orientation.equals(Orientation.HORIZONTAL) && !orientation.equals(Orientation.VERTICAL)) || selected == null) {
            return;
        }

        // Make sure that if we split horizontally, the width is at least 600px so that each new height will be 300
        if (orientation.equals(Orientation.HORIZONTAL) && selected.getWidth() < 600) {
            return;
        }

        // Make sure that if we split vertically, the height is at least 600px so that each new height will be 300
        if (orientation.equals(Orientation.VERTICAL) && selected.getHeight() < 600) {
            return;
        }

        SplitPane splitPane = (SplitPane) selected.getParent().getParent();
        StartViewController startViewController = (StartViewController) views.get(START_VIEW);
        StartViewController clone = startViewController.clone();
        clone.showCloseButton(true);

        if (splitPane.getOrientation().equals(orientation)) {
            splitPane.getItems().add(clone);
        } else {
            splitPane.getItems().replaceAll(view -> {
                if (view.equals(selected)) {
                    SplitPane newSplitPane = new SplitPane(clone);
                    AnchorPane.setBottomAnchor(newSplitPane, 0d);
                    AnchorPane.setTopAnchor(newSplitPane, 0d);
                    AnchorPane.setLeftAnchor(newSplitPane, 0d);
                    AnchorPane.setRightAnchor(newSplitPane, 0d);
                    newSplitPane.setOrientation(orientation);
                    newSplitPane.getItems().addAll(view, clone);
                    return newSplitPane;
                }
                return view;
            });
        }

        if (viewCounter == 1) {
            selected.showCloseButton(true);
        }

        viewCounter++;
        setSelected(selected);
    }

    public void merge() {

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

    public boolean replace(final SplitableView currentView, final String name) {
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

    public void setSelected(SplitableView anchorPane) {
        if (viewCounter > 1) {
            selected.setId("view_not_selected");
            anchorPane.setId("view_selected");
        }
        selected = anchorPane;
    }
}
