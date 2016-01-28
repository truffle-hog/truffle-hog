package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import javafx.scene.control.ToolBar;

/**
 * The Basic class for alls BorderPane controllers... if needed we can also
 * implement a dispatch Command fucntion in here that sends all the commands
 * to the dispatch Thread
 *
 * @param <I>
 */
public abstract class ToolBarController<I extends IInteraction> extends ToolBar
        implements IViewController<I> {

    /** The wrapped instance of view controller notifier. **/
    private final INotifier<IUserCommand> viewControllerNotifier =
            new ViewControllerNotifier();

    /**
     * {@inheritDoc}
     */
    @Override
    public final void addListener(final IListener<IUserCommand> listener) {

        viewControllerNotifier.addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void removeListener(final IListener<IUserCommand> listener) {
        viewControllerNotifier.removeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void notifyListeners(final IUserCommand message) {
        viewControllerNotifier.notifyListeners(message);
    }
}
