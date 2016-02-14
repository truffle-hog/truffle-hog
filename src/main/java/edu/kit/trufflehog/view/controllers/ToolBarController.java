package edu.kit.trufflehog.view.controllers;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToolBar;

import java.io.IOException;

/**
 * <p>
 *     The ToolBarController is the Basic ToolBar class that will be
 *     used by every ToolBar implementation in the application. Extending the
 *     javafx {@link ToolBar} it can be placed on every javafx Component.
 * </p>
 * @param <I> The type of interactions to be used on this toolbar
 */
public abstract class ToolBarController<I extends IInteraction> extends ToolBar
        implements IViewController<I> {

    public ToolBarController(String fxmlFile) {

        final FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /** The wrapped instance of view controller notifier. **/
    private final INotifier<IUserCommand> viewControllerNotifier =
            new ViewControllerNotifier();

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean addListener(final IListener<IUserCommand> listener) {

        return viewControllerNotifier.addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean removeListener(final IListener<IUserCommand> listener) {
        return viewControllerNotifier.removeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void notifyListeners(final IUserCommand message) {
        viewControllerNotifier.notifyListeners(message);
    }
}
