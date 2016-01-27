package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import javafx.scene.layout.BorderPane;



/**
 * <p>
 * The Basic abstraction for alls BorderPane controllers. Every abstraction
 * for javafx Components wrap a {@link ViewControllerNotifier} instance for
 * implementation of the specific operations.
 * </p>
 * @param <I>
 */
public abstract class BorderPaneController<I extends IInteraction> extends
        BorderPane implements IViewController<I> {

    private final INotifier<IUserCommand> viewControllerNotifier =
            new ViewControllerNotifier();

    @Override
    public void addListener(final IListener<IUserCommand> listener) {

        viewControllerNotifier.addListener(listener);
    }

    @Override
    public void removeListener(final IListener<IUserCommand> listener) {
        viewControllerNotifier.removeListener(listener);
    }

    @Override
    public void notifyListeners(final IUserCommand message) {
        viewControllerNotifier.notifyListeners(message);
    }

}
