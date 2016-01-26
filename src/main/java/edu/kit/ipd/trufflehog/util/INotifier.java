package edu.kit.ipd.trufflehog.util;

/**
 * <p>
 *     The INotifier interface works together with {@link Notifier} and {@link IListener} to create the main
 *     communication method between threads in TruffleHog. This communication method is a variation of the observer design
 *     pattern where messages sent from the subject to the observer include a parameter of type {@code M}. The
 *     {@code INotifier} is the subject in this case. {@code IListener}s register with the {@code INotifier} and can
 *     then be notified through the {@code notifyListeners} method. Unlike in the classic observer pattern, the
 *     {@code notifyListeners} method offers the possibility to pass along a parameter of type {@code M}.
 * </p>
 * <p>
 *     The {@code INotifier} is the abstraction from the {@link Notifier}. If a class needs to be notified and cannot
 *     extend it, it should implement {@code INotifier} and its methods should be delegated to the actual Notifier
 *     class.
 * </p>
 *
 * @param <M> The type of message to receive.
 */
public interface INotifier<M> {

    /**
     * <p>
     *    Register an {@code IListener} with this {@code INotifier}. This {@code IListener} will then be notified on the
     *    {@link #notifyListeners(M)} method call.
     * </p>
     *
     * @param listener The {@code IListeners} to register with this {@code INotifier}.
     */
    void addListener(IListener listener);

    /**
     * <p>
     *    Removes an {@code IListener} from this {@code INotifier}. This {@code IListener} will then not be notified
     *    anymore on the {@link #notifyListeners(M)} method call.
     * </p>
     *
     * @param listener The {@code IListeners} to register with this {@code INotifier}.
     */
    void removeListener(IListener listener);

    /**
     * <p>
     *     Notifies all {@code IListener}s that are registered with this {@code INotifier}. It sends along a message
     *     of type {@code M}
     * </p>
     *
     * @param message The message to send along with the notification to each {@code IListener}.
     */
    void notifyListeners(M message);
}
