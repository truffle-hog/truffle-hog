package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 *     Tests the saving of the replay logs.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ReplayLoggerTest {
    private ReplayLogger replayLogger;
    private File replayLogFolder;

    /**
     * <p>
     *     Sets the test up
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        replayLogger = new ReplayLogger();
        replayLogFolder = new File("./test-replaylogging");
        if (!replayLogFolder.exists()) {
            replayLogFolder.mkdir();
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
        if (replayLogFolder.exists()) {
            FileUtils.deleteDirectory(replayLogFolder);
        }

        replayLogger = null;
    }

    /**
     * <p>
     *     Creates multiple random replay logs serializes them, deserializes them and sees if they are still the same
     *     object.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testSaveReplayLog() throws Exception {
        int randomLength = (int) (Math.random() * 10) + 10;
        for (int i = 0; i < randomLength; i++) {
            LinkedMap<IReplayCommand, Long> replayCommands = new LinkedMap<>();

            // Generate "random" replay log
            int randomLength2 = (int) (Math.random() * 10) + 1;
            for (int j = 0; j < randomLength2; j++) {
                long randomTime = (long) (Math.random() * 3);
                replayCommands.put(null, randomTime);
            }

            ReplayLog replayLog = replayLogger.createReplayLog(null, replayCommands);
            CaptureSession captureSession = new CaptureSession(replayLogFolder, Instant.now());
            replayLogger.saveReplayLog(replayLog, captureSession);

            // Find the file
            File[] replayLogs = replayLogFolder.listFiles();
            assertEquals(true, replayLogs.length == 1);
            File replayLogFile = replayLogs[0];

            // Deserialize the replay log
            FileInputStream inputFileStream = new FileInputStream(replayLogFile.getCanonicalPath());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);
            ReplayLog replayLogResult = (ReplayLog) objectInputStream.readObject();
            objectInputStream.close();
            inputFileStream.close();

            assertEquals(true, replayLogResult.equals(replayLog));

            // Delete the file for the next iteration
            replayLogFile.delete();
        }
    }
}