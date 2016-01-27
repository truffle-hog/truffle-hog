package de.fraunhofer.iosb.trufflehog.command.queue;

import de.fraunhofer.iosb.trufflehog.command.ICommand;

/**
 * Created by Infinity on 27.01.2016.
 */
public interface ICommandQueue {

    /**
     * This interface
     *
     * @param command
     * @param <T>
     */
    <T extends ICommand> void push(T command) throws InterruptedException;

    /**
     * <p>
     *     Gets the first element of the queue and removes it from the list.
     * </p>
     * @return
     */
    ICommand pop();

    boolean isEmpty();
}
