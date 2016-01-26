package edu.kit.ipd.trufflehog.util;

/**
 * <p>
 *     The IListener interface works together with {@link Notifier} and {@link INotifier} to create the main
 *     communication method between threads in TruffleHog. This communication method is a variation of the observer design
 *     pattern where messages sent from the subject to the observer include a parameter of type {@code M}. The
 *     {@code IListener} is the observer in this case. It registers with the {@code Notifier} and receives messages sent
 *     from the {@code Notifier} through the {@code receive} method.
 * </p>
 *
 * @param <M> The type of message to receive
 */
public interface IListener<M> {

    /**
     * <p>
     *     Gets a message from a {@code Notifier} this {@code IListener} is registered to along with a parameter of
     *     type {@code M}.
     * </p>
     *
     * @param m The message that is sent from the {@code Notifier} to the {@code IListener}
     */
    void recieve(M m);
}
