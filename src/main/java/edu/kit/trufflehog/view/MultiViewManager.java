package edu.kit.trufflehog.view;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

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

        final SplitPane splitPane = (SplitPane) selected.getParent().getParent();
        final SplitableView startViewController = views.get(START_VIEW);
        final SplitableView clone = startViewController.clone();
        clone.showCloseButton(true);

        if (splitPane.getOrientation().equals(orientation)) {
            splitPane.getItems().add(clone);
        } else {
            splitPane.getItems().replaceAll(view -> {
                if (view.equals(selected)) {
                    final SplitPane newSplitPane = new SplitPane();
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
        setSelected(selected, 1);
    }

    public void merge() {
       merge(selected);
    }

    public void merge(SplitableView selected) {
        if (viewCounter == 1) {
            return;
        }

        SplitPane splitPane = (SplitPane) selected.getParent().getParent();
        splitPane.getItems().remove(selected);

        if (splitPane.getItems().size() == 1) {
            final Node child = splitPane.getItems().get(0);
            SplitPane parentSplitPane;
            try {
                parentSplitPane = (SplitPane) splitPane.getParent().getParent();
            } catch (ClassCastException e) {
                parentSplitPane = null;
            }

            if (parentSplitPane != null) {
                parentSplitPane.getItems().replaceAll(node -> {
                    if (node.equals(splitPane)) {
                        return child;
                    }
                    return node;
                });
            }

            if (viewCounter == 2) {
                final SplitableView splitableView = (SplitableView) child;
                if (!(splitableView instanceof LiveViewViewController)) { // I don't know how else to do this but instanceof
                    splitableView.showCloseButton(false);
                }
                setSelected(splitableView, 2);
            }
        }

        viewCounter--;
    }

    public boolean replace(final SplitableView currentView, final String name) {
        if (views.containsKey(name)) {
            SplitPane splitPane = (SplitPane) currentView.getParent().getParent();

            splitPane.getItems().replaceAll(view -> {
                if (view.equals(currentView)) {
                    SplitableView splitableView = views.get(name).clone();
                    if (selected.equals(currentView)) {
                        setSelected(splitableView, 1);
                    }
                    return splitableView;
                }
                return view;
            });

            return true;
        }

        return false;
    }

    public void setSelected(SplitableView anchorPane, int limit) {
        if (viewCounter > limit) {
            selected.setId("view_not_selected");
            anchorPane.setId("view_selected");
        } else if (selected != null) {
            selected.setId("view_not_selected");
            anchorPane.setId("view_not_selected");
        }
        selected = anchorPane;
    }

    public int getViewCounter() {
        return viewCounter;
    }

}
