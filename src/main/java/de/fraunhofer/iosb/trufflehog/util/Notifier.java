package de.fraunhofer.iosb.trufflehog.util;

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
 */
public abstract class Notifier<M> implements INotifier<M> {

    private final Collection<IListener<M>> listeners = new ConcurrentLinkedQueue<>();

    @Override
    public void addListener(IListener<M> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(IListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(M message) {
        listeners.forEach(listener -> listener.receive(message));
    }
}
