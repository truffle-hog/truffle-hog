package edu.kit.trufflehog.util;

/**
 * <p>
 *     The INotifier interface works together with {@link Notifier} and {@link IListener} to create the main
 *     communication method between threads in TruffleHog. This communication method is a variation of the observer design
 *     pattern where messages sent from the subject to the observer include a parameter of type M. The
 *     INotifier is the subject in this case. IListeners register with the INotifier and can
 *     then be notified through the notifyListeners method. Unlike in the classic observer pattern, the
 *     notifyListeners method offers the possibility to pass along a parameter of type M.
 * </p>
 * <p>
 *     The INotifier is the abstraction from the {@link Notifier}. If a class needs to be notified and cannot
 *     extend it, it should implement INotifier and its methods should be delegated to the actual Notifier
 *     class.
 * </p>
 *
 * @param <M> The type of message to receive.
 */
public interface INotifier<M> {

    /**
     * <p>
     *    Register an IListener with this INotifier. This IListener will then be notified on the
     *    {@link #notifyListeners(M)} method call.
     * </p>
     *
     * @param listener The IListeners to register with this INotifier.
     */
    void addListener(IListener listener);

    /**
     * <p>
     *    Removes an IListener from this INotifier. This IListener will then not be notified
     *    anymore on the {@link #notifyListeners(M)} method call.
     * </p>
     *
     * @param listener The IListeners to register with this INotifier.
     */
    void removeListener(IListener listener);

    /**
     * <p>
     *     Notifies all IListeners that are registered with this INotifier. It sends along a message
     *     of type M
     * </p>
     *
     * @param message The message to send along with the notification to each IListener.
     */
    void notifyListeners(M message);
}
