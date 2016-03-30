package edu.kit.trufflehog.view.controllers;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;


/**
 * <p>
 *      The basic abstraction for StackPane controllers.
 * </p>
 */
public abstract class StackPaneController<I extends IInteraction> extends StackPane implements IViewController<I> {

    /**
     * <p>
     *     The wrapped instance of view controllers notifier.
     * </p>
     */
    private final INotifier<IUserCommand> viewControllerNotifier;

    /**
     * <p>
     *     Creates a new AnchorPaneController based on the given fxml file.
     * </p>
     *
     * @param fxmlFile The fxml file to create the AnchorPaneController from.
     */
    public StackPaneController(String fxmlFile) {

        viewControllerNotifier = new ViewControllerNotifier(fxmlFile, this);
    }

    @Override
    public boolean addListener(IListener<IUserCommand> listener) {
        return viewControllerNotifier.addListener(listener);
    }

    @Override
    public boolean removeListener(IListener<IUserCommand> listener) {
        return viewControllerNotifier.removeListener(listener);
    }

    @Override
    public void notifyListeners(IUserCommand message) {
        viewControllerNotifier.notifyListeners(message);
    }
}
