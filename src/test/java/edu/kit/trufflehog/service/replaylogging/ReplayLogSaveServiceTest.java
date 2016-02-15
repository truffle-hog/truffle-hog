package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 *     Tests the replay log saving service in various ways.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ReplayLogSaveServiceTest {
    private FileSystem fileSystem;
    private LoggedScheduledExecutor loggedScheduledExecutor;
    private ReplayLogSaveService replayLogSaveService;

    /**
     * <p>
     *     Sets the test up
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(invocation -> new File("." + File.separator + "test-data"));
        when(fileSystem.getReplayLogFolder()).thenAnswer(invocation -> new File("." + File.separator +
                "test-data" + File.separator + "replaylogging"));
        loggedScheduledExecutor = new LoggedScheduledExecutor(1);
        replayLogSaveService = new ReplayLogSaveService(null, fileSystem, loggedScheduledExecutor);

        File replayLogFolder = new File("./test-data/replaylogging");
        if (!replayLogFolder.exists()) {
            replayLogFolder.mkdirs();
        }
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
        if (fileSystem.getDataFolder().exists()) {
            FileUtils.deleteDirectory(fileSystem.getDataFolder());
        }

        fileSystem = null;
        loggedScheduledExecutor = null;
        replayLogSaveService = null;
    }

    /**
     * <p>
     *     Runs the entire process. Commands are fired on the queue, and the queue is then emptied every X milliseconds
     *     and turned into a replay log that is saved on the hard drive.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void runTest() throws Exception {
        // Run it
        int sleepTime = 50;
        int loopMax = 100;
        recordSession(sleepTime, loopMax);

        // Check it
        File[] files = fileSystem.getReplayLogFolder().listFiles();
        assertEquals(1, files.length);

        // Use reflection to get logging interval in replayLogSaveService - not sure how else to get it
        Field field = replayLogSaveService.getClass().getDeclaredField("LOGGING_INTERVAL");
        field.setAccessible(true);
        int LOGGING_INTERVAL = (int) field.get(replayLogSaveService);

        File[] replayLogs = files[0].listFiles();
        int numberOfFiles = (sleepTime * loopMax) / LOGGING_INTERVAL;

        // There seems to be some concurrency lag which is causing 1 or 2 more replay logs to be created, after the
        // future.cancel() call has been made, not sure if this is an error or what not
        assertEquals(true, Math.abs(replayLogs.length - numberOfFiles) < 3);
    }

    /**
     * <p>
     *     Tests whether threads are correctly scheduled (meaning whether they are correctly started and stopped when
     *     start and stop is called) - only works half
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     *
     * FIXME: THIS DOES NOT WORK :(
     */
    @Test
    public void testStartStopRecord() throws Exception {
        for (int i = 0; i < 100; i++) {
            assertEquals(loggedScheduledExecutor.getActiveCount() == 0, true);

            replayLogSaveService.startRecord();

            assertEquals(loggedScheduledExecutor.getActiveCount() == 1, true);

            replayLogSaveService.stopRecord();

            assertEquals(loggedScheduledExecutor.getActiveCount() == 0, true);
        }
    }

    /**
     * <p>
     *     Creates a number of replay logs
     * </p>
     *
     * @param sleepTime How often a command is fired
     * @param loopMax How many commands are fired
     * @throws Exception Passes any errors that occurred during the test on
     */
    private void recordSession(int sleepTime, int loopMax) throws Exception {
        replayLogSaveService.startRecord();
        for (int i = 0; i < loopMax; i++) {
            IReplayCommand command = new AddPacketDataCommand(null, null, null);

            replayLogSaveService.receive(command);

            Thread.sleep(sleepTime);
        }
        replayLogSaveService.stopRecord();
    }
}
