package de.fraunhofer.iosb.trufflehog.command.queue;

import de.fraunhofer.iosb.trufflehog.command.ICommand;

/**
 * Created by Infinity on 27.01.2016.
 */
public interface ICommandQueue {

    /**
     *
     * @param command
     * @param <T>
     */
    <T extends ICommand> void push(T command) throws InterruptedException;

    ICommand pop();

    boolean isEmpty();
}
