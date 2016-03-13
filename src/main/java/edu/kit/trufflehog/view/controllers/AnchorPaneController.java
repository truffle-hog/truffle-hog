package edu.kit.trufflehog.view.controllers;


import edu.kit.trufflehog.interaction.IInteraction;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


/**
 * <p>
 *      The basic abstraction for AnchorPane controllers. This is the super class of the
 *      {@link AnchorPaneInteractionController} and it does not have an {@link IInteraction} because it does not
 *      interact with the model.
 * </p>
 */
public abstract class AnchorPaneController extends AnchorPane {

    /**
     * <p>
     *     Creates a new AnchorPaneController based on the given fxml file.
     * </p>
     *
     * @param fxmlFile The fxml file to create the AnchorPaneController from.
     */
    public AnchorPaneController(String fxmlFile) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
