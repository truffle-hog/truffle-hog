package edu.kit.trufflehog.command.queue;

import edu.kit.trufflehog.command.ICommand;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

/**
 * @author Mark Giraud
 * @version 0.0
 */
public class CommandQueueManagerTest {

    private CommandQueueManager manager;

    @Before
    public void setUp() {
        manager = new CommandQueueManager();
    }

    @After
    public void tearDown() {

    }

    /**
     * <p>
     *     Checks if the getNextQueue() method returns null if no queues are registered.
     * </p>
     * @throws Exception
     */
    @Test
    public void testGetNextQueueOnEmptyManager() throws Exception {
        assertNull(manager.getNextQueue());
    }

    /**
     * <p>
     *     Checks if getNextQueue() blocks, if there are no elements in any queue
     * </p>
     * @throws Exception
     */
    @Test
    public void testIfGetNextQueueBlocks() throws Exception {

        ICommandQueue cmdQueue = mock(ICommandQueue.class);
        manager.registerQueue(cmdQueue);

        Thread testRunner = new Thread(() -> {
            try {
                manager.getNextQueue();
            } catch (InterruptedException ignored) {

            }
        });

        testRunner.start();

        Thread.sleep(1000);

        assertEquals(Thread.State.WAITING, testRunner.getState());
        testRunner.interrupt();

        // add another queue and see if it still works
        testRunner = new Thread(() -> {
            try {
                manager.getNextQueue();
            } catch (InterruptedException ignored) {

            }
        });

        ICommandQueue cmdQueue2 = mock(ICommandQueue.class);
        manager.registerQueue(cmdQueue2);

        testRunner.start();

        Thread.sleep(1000);

        assertEquals(Thread.State.WAITING, testRunner.getState());
        testRunner.interrupt();
    }


    //TODO: Fix this test. Mockito doesn't seem to support multithreading??
    /**
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void testIfManagerBlocksAfterRemovingElements() throws Exception {
        ICommandQueue cmdQueue = mock(ICommandQueue.class);
        manager.registerQueue(cmdQueue);
        ICommandQueue cmdQueue2 = mock(ICommandQueue.class);
        manager.registerQueue(cmdQueue2);

        // Add 2 elements
        manager.notifyNewElement();
        manager.notifyNewElement();

        // mock the queue so that it returns true for isEmpty after 2 mocked elements have been removed
        when(cmdQueue.isEmpty()).thenReturn(false, false, true);
        // mock the queue so that it returns 2 mock objects for commands and after that null
        when(cmdQueue.pop()).thenAnswer(invocation -> {
            manager.notifyRemovedElement();
            System.out.println("Popped");
            return mock(ICommand.class);
        }).thenAnswer(invocation -> {
            System.out.println("Popped");
            manager.notifyRemovedElement();
            return mock(ICommand.class);
        }).thenAnswer(invocation1 -> {
            System.out.println("Popped");
            throw new NoSuchElementException();
        });

        Thread testRunner = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    System.out.println("Popping");
                    manager.getNextQueue().pop();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        testRunner.start();

        Thread.sleep(1000);

        assertEquals(Thread.State.WAITING, testRunner.getState());
        testRunner.interrupt();
    }

}
