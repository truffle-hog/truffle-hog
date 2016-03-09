package edu.kit.trufflehog.view.controllers;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
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
public abstract class MenuBarController<I extends IInteraction> extends MenuBar implements IViewController<I> {

    public MenuBarController(String fxmlFile) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * <p>
     *     The wrapped instance of view controller notifier.
     * </p>
     */
    private final INotifier<IUserCommand> viewControllerNotifier = new ViewControllerNotifier();

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
