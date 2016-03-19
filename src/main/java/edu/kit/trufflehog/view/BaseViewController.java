package edu.kit.trufflehog.view;

import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class BaseViewController extends StartViewController {

    /**
     * <p>
     *     Creates a new BaseViewController with the given fxmlFileName. The fxml file has to be in the same namespace
     *     as the BaseViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     * @param liveItems
     * @param captureItems
     * @param viewSwitcher
     */
    public BaseViewController(final String fxmlFileName,
                              final ObservableList<String> liveItems,
                              final ObservableList<String> captureItems,
                              final ViewSwitcher viewSwitcher) {
        super(fxmlFileName, liveItems, captureItems, viewSwitcher);

        addCloseButton(viewSwitcher);
    }

    /**
     * <p>
     *     Adds a close button.
     * </p>
     */
    private void addCloseButton(ViewSwitcher viewSwitcher) {
        final ImageButton closeButton = new ImageButton("close.png");

        closeButton.setScaleX(0.8);
        closeButton.setScaleY(0.8);

        AnchorPane.setTopAnchor(closeButton, 10d);
        AnchorPane.setLeftAnchor(closeButton, 10d);

        getChildren().add(closeButton);
    }
}
