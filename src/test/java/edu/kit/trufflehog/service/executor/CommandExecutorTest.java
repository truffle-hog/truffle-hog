package edu.kit.trufflehog.service.executor;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.command.usercommand.IUserCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * This class houses all test cases for the {@link CommandExecutor}
 * @author Mark Giraud
 * @version 1.0
 */
public class CommandExecutorTest {

    private CommandExecutor executor;

    @Before
    public void setUp() throws Exception {
        executor = new CommandExecutor();
    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * <p>
     *     This test checks, if the {@link CommandExecutor} executes the received commands and if it is done in
     *     the correct order.
     * </p>
     * @throws Exception
     */
    @Test
    public void testRun() throws Exception {

        List<ICommand> mockedCommands = new LinkedList<>();

        final int[] callNumber = {0};

        ICommand[] commandCallOrder = new ICommand[30];

        for (int i = 0; i < 10; i++) {
            ITruffleCommand tCommand = mock(ITruffleCommand.class);
            doAnswer(invocation -> {
                commandCallOrder[callNumber[0]] = tCommand;
                callNumber[0]++;
                return null;
            }).when(tCommand).execute();

            IUserCommand uCommand = mock(IUserCommand.class);
            doAnswer(invocation -> {
                commandCallOrder[callNumber[0]] = uCommand;
                callNumber[0]++;
                return null;
            }).when(uCommand).execute();

            mockedCommands.add(tCommand);
            mockedCommands.add(uCommand);

            executor.asTruffleCommandListener().receive(tCommand);
            executor.asUserCommandListener().receive(uCommand);
        }

        for (int i = 0; i < 10; i++) {
            ITruffleCommand tCommand = mock(ITruffleCommand.class);
            doAnswer(invocation -> {
                commandCallOrder[callNumber[0]] = tCommand;
                callNumber[0]++;
                return null;
            }).when(tCommand).execute();

            mockedCommands.add(tCommand);
            executor.asTruffleCommandListener().receive(tCommand);
        }

        Thread testRunner = new Thread(executor);
        testRunner.start();

        Thread.sleep(1000);

        // Check if the thread is blocking after executing all the commands
        assertEquals(Thread.State.WAITING, testRunner.getState());

        testRunner.interrupt();

        int i = 0;
        for (ICommand command : mockedCommands) {

            // command executed?
            verify(command).execute();

            // executed in correct order?
            assertEquals(command, commandCallOrder[i]);
            i++;
        }

    }

    @Test(expected = NullPointerException.class)
    public void testTCListenerNullMessage() {
        executor.asTruffleCommandListener().receive(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUCListenerNullMessage() {
        executor.asUserCommandListener().receive(null);
    }

    /**
     * <p>
     *     This test checks if the asUserCommandListener method works correctly. This is done
     *     by sending command mock objects to the listener and then checking if they are run once by the
     *     service. (To check if they were received properly)
     * </p>
     * @throws Exception
     */
    @Test
    public void testAsUserCommandListener() throws Exception {

        List<IUserCommand> mockedCommands = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            IUserCommand command = mock(IUserCommand.class);
            mockedCommands.add(command);
            executor.asUserCommandListener().receive(command);
        }

        Thread testRunner = new Thread(executor);
        testRunner.start();

        Thread.sleep(1000);

        testRunner.interrupt();

        for (IUserCommand command : mockedCommands) {
            verify(command).execute();
        }
    }

    /**
     * <p>
     *     This test checks if the asTruffleCommandListener method works correctly. This is done
     *     by sending command mock objects to the listener and then checking if they are run once by the
     *     service. (To check if they were received properly)
     * </p>
     * @throws Exception
     */
    @Test
    public void testAsTruffleCommandListener() throws Exception {

        List<ITruffleCommand> mockedCommands = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            ITruffleCommand command = mock(ITruffleCommand.class);
            mockedCommands.add(command);
            executor.asTruffleCommandListener().receive(command);
        }

        Thread testRunner = new Thread(executor);
        testRunner.start();

        Thread.sleep(1000);

        testRunner.interrupt();

        for (ITruffleCommand command : mockedCommands) {
            verify(command).execute();
        }
    }
}