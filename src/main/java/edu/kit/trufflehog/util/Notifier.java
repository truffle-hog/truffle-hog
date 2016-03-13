package edu.kit.trufflehog.util;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <p>
 *     The Notifier class works together with {@link INotifier} and {@link IListener} to create the main
 *     communication method between threads in TruffleHog. This communication method is a variation of the observer design
 *     pattern where messages sent from the subject to the observer include a parameter of type M. The
 *     Notifier is the subject in this case. IListeners register with the Notifier and can
 *     then be notified through the notifyListeners method. Unlike in the classic observer pattern, the
 *     notifyListeners method offers the possibility to pass along a parameter of type M.
 * </p>
 * <p>
 *     The Notifier is the implementation of the {@link INotifier}. If a class can extend the Notifier
 *     it should.
 * </p>
 *
 * @param <M> The type of message to receive.
 * @author Mark Giraud
 * @version 1.0
 */
public abstract class Notifier<M> implements INotifier<M> {

    /**
     * This needs to be a concurrent queue! Do not change!
     */
    private final Collection<IListener<M>> listeners = new ConcurrentLinkedQueue<>();

    @Override
    public boolean addListener(final IListener<M> listener) {
        if (listener == null)
            throw new NullPointerException("Listener to add should not be null!");

        return listeners.add(listener);
    }

    @Override
    public boolean removeListener(final IListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener to should must not be null!");

        return listeners.remove(listener);
    }

    @Override
    public void notifyListeners(final M message) {
        if (message == null)
            throw new NullPointerException("Message to be sent should not be null!");

        listeners.forEach(listener -> listener.receive(message));
    }
}
