package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by julianbrendl on 2/14/16.
 */
public class CommandLoggerTest {
    private CommandLogger commandLogger;

    /**
     * <p>
     *     Sets the test up
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        commandLogger = new CommandLogger();
    }

    /**
     * <p>
     *     Deletes all resources after each test (for example data folder)
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @After
    public void tearDown() throws Exception {
        commandLogger = null;
    }

    /**
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testAddCommand() throws Exception {
        Instant now =  Instant.now();

        int randomLength = (int) (Math.random() * 10) + 10;
        for (int i = 0; i < randomLength; i++) {
            IReplayCommand command = mock(AddPacketDataCommand.class);
            when(command.createdAt()).thenAnswer(invocation -> Instant.now());

            commandLogger.addCommand(command, now);
        }

        commandLogger.swapMaps();

        LinkedMap<IReplayCommand, Long> commands = commandLogger.createCommandLog();

        assertEquals(commands.size(), randomLength);

        long currentLong = commands.getValue(0);
        for (Long nextLong : commands.values()) {
            assertEquals(currentLong <= nextLong, true);
            currentLong = nextLong;
        }
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testSwapMaps() throws Exception {
        Instant now =  Instant.now();

        for (int i = 0; i < 10; i++) {
            int randomLength1 = (int) (Math.random() * 10) + 10;
            for (int j = 0; j < randomLength1; j++) {
                IReplayCommand command = mock(AddPacketDataCommand.class);
                when(command.createdAt()).thenAnswer(invocation -> Instant.now());

                commandLogger.addCommand(command, now);
            }
            commandLogger.swapMaps();
            LinkedMap<IReplayCommand, Long> commands = commandLogger.createCommandLog();
            assertEquals(commands.size(), randomLength1);

            int randomLength2 = (int) (Math.random() * 10) + 10;
            for (int j = 0; j < randomLength2; j++) {
                IReplayCommand command = mock(AddPacketDataCommand.class);
                when(command.createdAt()).thenAnswer(invocation -> Instant.now());

                commandLogger.addCommand(command, now);
            }
            commandLogger.swapMaps();
            commands = commandLogger.createCommandLog();
            assertEquals(commands.size(), randomLength2);
        }
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testCreateCommandLog() throws Exception {

    }
}