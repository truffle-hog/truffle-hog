/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.trufflehog.view;

import edu.kit.trufflehog.presenter.BaseNetworkFactory;
import edu.kit.trufflehog.presenter.NetworkType;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

/**
 * <p>
 *     The ViewSplitter manages all interactions between views. That means it splits views, merges them together,
 *     tracks the currently selected view etc.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ViewSplitter {
    private final BaseNetworkFactory baseNetworkFactory;
    private SplitableView selected;
    private int viewCounter = 1;

    /**
     * <p>
     *     Create a new ViewSplitter.
     * </p>
     */
    public ViewSplitter(BaseNetworkFactory baseNetworkFactory) {
        this.selected  = null;
        this.baseNetworkFactory = baseNetworkFactory;
    }

    /**
     * <p>
     *     Split the current view either horizontally or vertically depending on the orientation that is given. Thus
     *     the only two supported orientations are Orientation.HORIZONTAL and Orientation.VERTICAL. If the current
     *     view is too small to be split (that means smaller than 600 pixels in the direction of interest) the split
     *     does not occur.
     * </p>
     *
     * @param orientation The direction in which the view should be split up.
     */
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

        // Get the parent split pane and create the new view that
        final SplitPane splitPane = (SplitPane) selected.getParent().getParent();
        final SplitableView startViewController = (SplitableView) baseNetworkFactory.createInstance(NetworkType.START);
        startViewController.showCloseButton(true);

        // Insert the new view into the window
        if (splitPane.getOrientation().equals(orientation)) {
            // This is easy, if the orientation is the same, just plain up add it
            splitPane.getItems().add(startViewController);
        } else {
            // Here we need to create a new split pane that holds the old (selected) view and the just created view
            splitPane.getItems().replaceAll(view -> {
                if (view.equals(selected)) {
                    final SplitPane newSplitPane = new SplitPane();
                    newSplitPane.setOrientation(orientation);
                    newSplitPane.getItems().addAll(view, startViewController);
                    return newSplitPane;
                }
                return view;
            });
        }

        // If the selected view was alone up to this point, then show the delete button
        if (viewCounter == 1) {
            selected.showCloseButton(true);
        }

        viewCounter++;
        setSelected(selected, 1);
    }

    /**
     * <p>
     *     Merge two views together. That means the currently selected view will be removed. This only works if
     *     there are at least two views existing.
     * </p>
     */
    public void merge() {
       merge(selected);
    }

    /**
     * <p>
     *     Merge two views together. That means the view received as the parameter will be removed. This only works if
     *     there are at least two views existing.
     * </p>
     *
     * @param selected The view that should be deleted.
     */
    public void merge(SplitableView selected) {
        // Don't remove our only view!!
        if (viewCounter == 1) {
            return;
        }

        // We start out by simply removing the view from its parent split pane
        SplitPane splitPane = (SplitPane) selected.getParent().getParent();
        splitPane.getItems().remove(selected);

        // Now if the parent split pane only has 1 child left, we try to remove from that split pane and append it to
        // the parent's parent split pane, if one exists. We do this because we created a split pane in the split
        // method in case the orientation didn't match. Thus we now need to delete it again.
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

            // Additionally, we need to do some extra things if we now only have one view left like removing the blue
            // border and maybe the close button.The reason why we check if the viewCounter is equal to 2 and not 1,
            // is because it has not been decremented yet.
            if (viewCounter == 2) {
                final SplitableView splitableView = (SplitableView) child;

                // I don't know how else to do this but instanceof
                if (!(splitableView instanceof NetworkViewController)) {
                    splitableView.showCloseButton(false);
                }
                setSelected(splitableView, 2);
            }
        }

        viewCounter--;
    }

    /**
     * <p>
     *     Replace the view passed in as the parameter with the view matching the given name, if it has been added to
     *     the ViewSplitter's list of existing views.
     * </p>
     *
     * @param currentView The view that should be replaced with one from the internal list.
     * @param name The name of the view that should be replaced with the "current view" passed over as a parameter.
     * @return True if everything was successful, and false on an error.
     */
    public boolean replace(final SplitableView currentView, final NetworkType name) {
        SplitPane splitPane = (SplitPane) currentView.getParent().getParent();

        splitPane.getItems().replaceAll(view -> {
            if (view.equals(currentView)) {
                SplitableView splitableView = (SplitableView) baseNetworkFactory.createInstance(name);
                if (selected.equals(currentView)) {
                    setSelected(splitableView);
                }
                return splitableView;
            }
            return view;
        });
        return true;
    }

    /**
     * <p>
     *     Set the view passed as the parameter as the current view. This method also makes sure that the border
     *     coloring is right. Additionally the limit to the number of existing views has to be set for the view to
     *     get a blue border (this is usually 1).
     * </p>
     *
     * @param splitableView The view that should be set as the current view.
     * @param limit The limit to the number of existing views for the view to get a blue border (this is usually 1).
     */
    private void setSelected(SplitableView splitableView, int limit) {
        if (viewCounter > limit) {
            selected.setId("view_not_selected");
            splitableView.setId("view_selected");
        } else if (selected != null) {
            selected.setId("view_not_selected");
            splitableView.setId("view_not_selected");
        }
        selected = splitableView;
    }

    /**
     * <p>
     *     Set the view passed as the parameter as the current view. This method also makes sure that the border
     *     coloring is right.
     * </p>
     *
     * @param splitableView The view that should be set as the current view.
     */
    public void setSelected(SplitableView splitableView) {
        setSelected(splitableView, 1);
    }

    /**
     * <p>
     *     Gets the current number of views in the window.
     * </p>
     *
     * @return The current number of views in the window.
     */
    public int getViewCounter() {
        return viewCounter;
    }
}
