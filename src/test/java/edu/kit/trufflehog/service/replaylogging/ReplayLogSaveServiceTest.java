package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.command.trufflecommand.AddPacketDataCommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * <p>
 *
 * </p>
 */
public class ReplayLogSaveServiceTest {
    private FileSystem fileSystem;
    private LoggedScheduledExecutor loggedScheduledExecutor;
    private ReplayLogSaveService replayLogSaveService;

    /**
     * <p>
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        fileSystem = new FileSystem();
        loggedScheduledExecutor = new LoggedScheduledExecutor(1);
        replayLogSaveService = new ReplayLogSaveService(null, fileSystem, loggedScheduledExecutor);
    }

    /**
     * <p>
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @After
    public void tearDown() throws Exception {
//        if (fileSystem.getDataFolder().exists()) {
//            FileUtils.deleteDirectory(fileSystem.getDataFolder());
//        }

        fileSystem = null;
        loggedScheduledExecutor = null;
        replayLogSaveService = null;
    }

    @Test
    public void runTest() throws Exception {
        replayLogSaveService.startRecord();

        for (int i = 0; i < 200; i++) {
            ICommand command = mock(AddPacketDataCommand.class);
            replayLogSaveService.receive(command);

            Thread.sleep(50);
        }

        replayLogSaveService.stopRecord();
    }
}
