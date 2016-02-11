package edu.kit.trufflehog.command.queue;

import edu.kit.trufflehog.command.ICommand;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

/**
 *
 * <p>
 *     Test for the {@link CommandQueue} class.
 * </p>
 *
 * @author Mark Giraud
 * @version 1.0
 */
public class CommandQueueTest {

    private CommandQueue commandQueue;
    private CommandQueueManager mockedManager;

    @Before
    public void setUp() {
        mockedManager = mock(CommandQueueManager.class);
        commandQueue = new CommandQueue(mockedManager);
    }

    @After
    public void tearDown() {
    }

    /**
     * Checks if the queue registers itself with the {@link CommandQueueManager}.
     * @throws Exception
     */
    @Test
    public void testConstruction() throws Exception {
        commandQueue = new CommandQueue(mockedManager);
        verify(mockedManager).registerQueue(commandQueue);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructionWithNull() throws Exception {
        commandQueue = new CommandQueue(null);
    }

    /**
     * <p>
     *     This test verifies if the {@link CommandQueue} correctly notifies the manager that a new object was added.
     *     It also checks, if the element is returned properly after using pop().
     * </p>
     * @throws Exception
     */
    @Test
    public void addOneCommandToQueue() throws Exception {

        ICommand command = mock(ICommand.class);

        commandQueue.push(command);

        verify(mockedManager).notifyNewElement();

        assertEquals(command, commandQueue.pop());
    }

    /**
     * <p>
     *     This test verifies if the {@link CommandQueue} correctly notifies the manager that an object was removed.
     * </p>
     * @throws Exception
     */
    @Test
    public void popOneElementFromQueue() throws Exception {

        ICommand command = mock(ICommand.class);

        commandQueue.push(command);

        commandQueue.pop();

        verify(mockedManager).notifyRemovedElement();
    }

    /**
     * <p>
     *     This test verifies that there are no interactions with the stored {@link ICommand} objects.
     * </p>
     * @throws Exception
     */
    @Test
    public void noInteractionsOnElements() throws Exception {

        ICommand command = mock(ICommand.class);
        ICommand command2 = mock(ICommand.class);

        commandQueue.push(command);
        commandQueue.pop();
        commandQueue.push(command2);
        commandQueue.push(command);
        commandQueue.pop();
        commandQueue.pop();

        verifyZeroInteractions(command);
    }

    /**
     * <p>
     *     Tests if the elements are managed in a first in first out order.
     * </p>
     * @throws Exception
     */
    @Test
    public void checkCorrectOrderOfElements() throws Exception {

        ICommand command1 = mock(ICommand.class);
        ICommand command2 = mock(ICommand.class);
        ICommand command3 = mock(ICommand.class);
        ICommand command4 = mock(ICommand.class);

        commandQueue.push(command1);
        commandQueue.push(command2);
        commandQueue.push(command3);
        commandQueue.push(command4);

        assertEquals(commandQueue.pop(), command1);
        assertEquals(commandQueue.pop(), command2);
        assertEquals(commandQueue.pop(), command3);
        assertEquals(commandQueue.pop(), command4);
    }

    /**
     * Checks if an empty queue correctly throws a {@link NoSuchElementException}.
     * @throws Exception
     */
    @Test(expected = NoSuchElementException.class)
    public void noElementsPop() throws Exception {

        commandQueue.pop();
    }

    /**
     * Checks if the isEmpty() method works correctly
     * @throws Exception
     */
    @Test
    public void checkIsEmpty() throws Exception {

        assertTrue(commandQueue.isEmpty());

        commandQueue.push(mock(ICommand.class));

        assertFalse(commandQueue.isEmpty());

        commandQueue.pop();

        assertTrue(commandQueue.isEmpty());
    }

    /**
     * <p>
     *     Checks if the queue correctly throw a {@link NullPointerException} when pushing null.
     * </p>
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void addNullElement() throws Exception {

        commandQueue.push(null);
    }
}
