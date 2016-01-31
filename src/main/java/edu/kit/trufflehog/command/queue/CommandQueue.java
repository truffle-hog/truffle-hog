package edu.kit.trufflehog.command.queue;

import edu.kit.trufflehog.command.ICommand;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * <p>
 *     This {@link ICommandQueue} implementation uses a {@link ConcurrentLinkedDeque}.
 *     It also automatically registers itself with a {@link CommandQueueManager} to allow the usage of it.
 * </p>
 */
public class CommandQueue implements ICommandQueue {

    private final Deque<ICommand> commandQueue = new ConcurrentLinkedDeque<>();

    private final CommandQueueManager manager;

    /**
     * <p>
     *     Creates a new CommandQueue object.
     * </p>
     *
     * @param commandQueueManager The instance of the {@link CommandQueueManager} that manages all the command queues.
     */
    public CommandQueue(CommandQueueManager commandQueueManager) {
        this.manager = commandQueueManager;
        this.manager.registerQueue(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param command The command to put onto the Queue.
     * @param <T>     The type of the command.
     * @throws InterruptedException
     */
    @Override
    public <T extends ICommand> void push(T command) throws InterruptedException {
        commandQueue.addLast(command);
        manager.notifyNewElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICommand pop() throws InterruptedException {
        manager.notifyRemovedElement();
        return commandQueue.removeFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return commandQueue.isEmpty();
    }
}
