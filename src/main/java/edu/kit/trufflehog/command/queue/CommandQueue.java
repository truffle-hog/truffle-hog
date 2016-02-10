package edu.kit.trufflehog.command.queue;

import edu.kit.trufflehog.command.ICommand;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * <p>
 *     This {@link ICommandQueue} implementation uses a {@link ConcurrentLinkedDeque}.
 *     It also automatically registers itself with a {@link CommandQueueManager} to allow the usage of it.
 * </p>
 *
 * @author Mark Giraud
 * @version 1.0
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

        if (commandQueueManager == null)
            throw new NullPointerException();

        this.manager = commandQueueManager;
        this.manager.registerQueue(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param command The command to put onto the Queue.
     * @param <T>     The type of the command.
     * @throws InterruptedException
     * @throws NullPointerException If the command to add is null.
     */
    @Override
    public <T extends ICommand> void push(T command) throws InterruptedException {
        if (command == null)
            throw new NullPointerException("Command object to be added may not be null!");

        commandQueue.addLast(command);
        manager.notifyNewElement();
    }

    /**
     * {@inheritDoc}
     * @throws InterruptedException
     * @throws java.util.NoSuchElementException If there are no elements in this queue.
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
