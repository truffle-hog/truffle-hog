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
 * <p>
 *     Tests the capabilities of the command logger, meaning whether it correctly manages its internal buffers.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
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
     * <p>
     *     Tests whether commands were added correctly by checking that they are ordered by the time they were added
     *     to the map FIFO style.
     * </p>
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

        assertEquals(randomLength, commands.size());

        long currentLong = commands.getValue(0);
        for (Long nextLong : commands.values()) {
            assertEquals(currentLong <= nextLong, true);
            currentLong = nextLong;
        }
    }

    /**
     * <p>
     *     Swaps the internal buffers (which are maps) 10 times to sees if they were swapped correctly
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testSwapMaps() throws Exception {
        for (int i = 0; i < 10; i++) {
            Instant now =  Instant.now();

            int randomLength1 = (int) (Math.random() * 10) + 10;
            for (int j = 0; j < randomLength1; j++) {
                IReplayCommand command = mock(AddPacketDataCommand.class);
                when(command.createdAt()).thenAnswer(invocation -> Instant.now());

                commandLogger.addCommand(command, now);
            }
            commandLogger.swapMaps();
            LinkedMap<IReplayCommand, Long> commands = commandLogger.createCommandLog();
            assertEquals(randomLength1, commands.size());

            int randomLength2 = (int) (Math.random() * 10) + 10;
            for (int j = 0; j < randomLength2; j++) {
                IReplayCommand command = mock(AddPacketDataCommand.class);
                when(command.createdAt()).thenAnswer(invocation -> Instant.now());

                commandLogger.addCommand(command, now);
            }
            commandLogger.swapMaps();
            commands = commandLogger.createCommandLog();
            assertEquals(randomLength2, commands.size());
        }
    }

    /**
     * <p>
     *     Currently not much to test - since this just returns what it finds in the buffer
     * </p>
     * @throws Exception
     */
    @Test
    public void testCreateCommandLog() throws Exception {
        testAddCommand(); //TODO: either remove test or do correctly once command compressor is done
    }
}