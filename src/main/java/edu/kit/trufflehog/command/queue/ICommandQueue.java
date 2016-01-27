package edu.kit.trufflehog.command.queue;

import edu.kit.trufflehog.command.ICommand;

/**
 * <p>
 *     This interface supplies general methods for any CommandQueue implementation.
 * </p>
 *
 * @author Mark Giraud
 * @version 0.0
 */
public interface ICommandQueue {

    /**
     * <p>
     *     Pushes the supplied command onto the queue.
     * </p>
     *
     * <p>
     *     The element will always be added to the end of the list. Any element that was added will be returned after
     *     all elements that were added before it if pop is called.
     * </p>
     *
     * @param command The command to put onto the Queue.
     * @param <T>     The type of the command.
     * @throws InterruptedException
     */
    <T extends ICommand> void push(T command) throws InterruptedException;

    /**
     * <p>
     *     Gets the first element of the queue and removes it from the list.
     * </p>
     *
     * @return The first command on the queue
     * @throws InterruptedException
     */
    ICommand pop() throws InterruptedException;

    boolean isEmpty();
}
