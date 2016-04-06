package edu.kit.trufflehog.view.controllers;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Map;


/**
 * <p>
 *      The basic abstraction for AnchorPane controllers. This is the super class of the
 *      {@link AnchorPaneInteractionController} and it does not have an {@link IInteraction} because it does not
 *      interact with the model.
 * </p>
 */
public class AnchorPaneController<I extends  IInteraction> extends AnchorPane implements IViewController<I> {

    /**
     * <p>
     *     The wrapped instance of view controllers notifier.
     * </p>
     */
    private final INotifier<IUserCommand> viewControllerNotifier;

    private final Map<I, IUserCommand> interactionMap;

    /**
     * <p>
     *     Creates a new AnchorPaneController based on the given fxml file.
     * </p>
     *
     * @param fxmlFile The fxml file to create the AnchorPaneController from.
     */
    public AnchorPaneController(String fxmlFile, Map<I, IUserCommand> interactionMap) {

        this.interactionMap = interactionMap;

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

    @Override
    public void addCommand(I interaction, IUserCommand command) {
        interactionMap.put(interaction, command);
    }

    public IUserCommand getCommand(I interaction) {
        return interactionMap.get(interaction);
    }
}
