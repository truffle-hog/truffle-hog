package edu.kit.trufflehog.command.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Mark Giraud
 * @version 0.0
 */
public class CommandQueueManager {

    private int registeredQueues;

    private int currentQueue;

    private final List<ICommandQueue> queues = new ArrayList<>();

    private final BlockingQueue availableElementQueue = new LinkedBlockingQueue<>();

    /**
     * Creates a mew CommandQueueManager
     */
    public CommandQueueManager() {
        registeredQueues = 0;
        currentQueue = 0;
    }

    protected void registerQueue(ICommandQueue queue) {
        queues.add(queue);
        registeredQueues++;
    }

    /**
     * <p>
     *     Gets the next non empty queue
     * </p>
     *
     * <p>
     *     This method gets the next non empty {@link ICommandQueue}
     * </p>
     * @return
     * @throws InterruptedException
     */
    public ICommandQueue getNextQueue() throws InterruptedException {

        if (registeredQueues == 0)
            return null;

        availableElementQueue.take();

        ICommandQueue current = queues.get(currentQueue);

        int timesLooped = 0;

        // Check for other queues if the current one is empty but don't do busy waiting (only loop over all once)
        while (current.isEmpty() && timesLooped <= registeredQueues) {
            currentQueue = (currentQueue + 1) % registeredQueues;
            timesLooped++;
        }

        current = queues.get(currentQueue);

        // next time we want a different queue
        currentQueue = (currentQueue + 1) % registeredQueues;

        return current;
    }

    public void notifyNewElement() throws InterruptedException {
        availableElementQueue.put(Boolean.TRUE);
    }
}
