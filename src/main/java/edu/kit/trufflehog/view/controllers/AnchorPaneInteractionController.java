package edu.kit.trufflehog.view.controllers;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;

/**
 * <p>
 *      An abstraction for AnchorPane controllers that need to wrap a {@link ViewControllerNotifier} in order
 *      to notify the model of any changes.
 * </p>
 *
 * @param <I> The type of interaction to be used in the ViewController
 *
 * @author Julian Brendl
 * @version 1.0
 */
public abstract  class AnchorPaneInteractionController<I extends IInteraction> extends AnchorPaneController
        implements IViewController<I> {
    private final INotifier<IUserCommand> viewControllerNotifier = new ViewControllerNotifier();

    /**
     * <p>
     *     Creates a new AnchorPaneController based on the given fxml file
     * </p>
     *
     * @param fxmlFile The fxml file to create the AnchorPaneController from.
     */
    public AnchorPaneInteractionController(String fxmlFile) {
        super(fxmlFile);
    }

    public abstract AnchorPaneInteractionController clone();

    @Override
    public final boolean addListener(final IListener<IUserCommand> listener) {
        return viewControllerNotifier.addListener(listener);
    }

    @Override
    public final boolean removeListener(final IListener<IUserCommand> listener) {
        return viewControllerNotifier.removeListener(listener);
    }

    @Override
    public final void notifyListeners(final IUserCommand message) {
        viewControllerNotifier.notifyListeners(message);
    }
}
