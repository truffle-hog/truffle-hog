package edu.kit.trufflehog.view;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;

import java.io.IOException;

/**
 * <p>
 *     The ToolBarViewController class is the view designed for all most important functionality that should be
 *     directly accessed by the user during the running program.
 * </p>
 */
public final class ToolBarViewController extends ToolBar {

    /**
     * <p>
     *     Creates a new ToolBarViewController with the given fxmlFileName. The fxml file has to be in the same
     *     namespace as the ToolBarViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public ToolBarViewController(final String fxmlFileName, Node... items) {
        super(items);
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
