package de.fraunhofer.iosb.trufflehog.command.queue;

import de.fraunhofer.iosb.trufflehog.command.ICommand;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * @author Mark Giraud
 * @version 0.0
 */
public class CommandQueue implements ICommandQueue {

    private final Deque<ICommand> commandQueue = new ConcurrentLinkedDeque<>();

    private final CommandQueueManager manager;

    public CommandQueue(CommandQueueManager commandQueueManager) {
        this.manager = commandQueueManager;
        this.manager.registerQueue(this);
    }

    @Override
    public <T extends ICommand> void push(T command) throws InterruptedException {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public ICommand pop() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
