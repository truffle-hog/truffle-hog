package edu.kit.trufflehog.util;

/**
 * <p>
 *     The IListener interface works together with {@link Notifier} and {@link INotifier} to create the main
 *     communication method between threads in TruffleHog. This communication method is a variation of the observer design
 *     pattern where messages sent from the subject to the observer include a parameter of type M. The
 *     IListener is the observer in this case. It registers with the Notifier and receives messages sent
 *     from the Notifier through the receive method.
 * </p>
 *
 * @param <M> The type of message to receive
 * @author Mark Giraud
 * @version 1.0
 */
public interface IListener<M> {

    /**
     * <p>
     *     Gets a message from a Notifier this IListener is registered to along with a parameter of
     *     type M.
     * </p>
     *
     * @param m The message that is sent from the Notifier to the IListener.
     */
    void receive(final M m);
}
