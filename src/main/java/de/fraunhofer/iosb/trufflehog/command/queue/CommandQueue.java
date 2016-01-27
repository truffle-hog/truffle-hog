package de.fraunhofer.iosb.trufflehog.command.queue;

import de.fraunhofer.iosb.trufflehog.command.ICommand;

/**
 * Created by Infinity on 27.01.2016.
 */
public class CommandQueue implements ICommandQueue {
    public CommandQueue(CommandQueueManager commandQueueManager) {

    }

    @Override
    public <T extends ICommand> void push(T command) throws InterruptedException {

    }

    @Override
    public ICommand pop() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
