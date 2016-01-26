package edu.kit.ipd.trufflehog.util;

/**
 * <p>
 *     The Notifier class works together with {@link INotifier} and {@link IListener} to create the main
 *     communication method between threads in TruffleHog. This communication method is a variation of the observer design
 *     pattern where messages sent from the subject to the observer include a parameter of type {@code M}. The
 *     {@code Notifier} is the subject in this case. {@code IListener}s register with the {@code Notifier} and can
 *     then be notified through the {@code notifyListeners} method. Unlike in the classic observer pattern, the
 *     {@code notifyListeners} method offers the possibility to pass along a parameter of type {@code M}.
 * </p>
 * <p>
 *     The {@code Notifier} is the implementation of the {@link INotifier}. If a class can extend the {@code Notifier}
 *     it should.
 * </p>
 *
 * @param <M> The type of message to receive.
 */
public abstract class Notifier<M> implements INotifier<M> {
    @Override
    public void addListener(IListener listener) {

    }

    @Override
    public void removeListener(IListener listener) {

    }

    @Override
    public void notifyListeners(M message) {

    }
}
