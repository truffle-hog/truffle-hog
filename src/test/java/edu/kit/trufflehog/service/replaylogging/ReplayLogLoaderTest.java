package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.graph.GraphProxy;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by julianbrendl on 2/14/16.
 */
public class ReplayLogLoaderTest {
    private FileSystem fileSystem;
    private GraphProxy graphProxy;
    private LoggedScheduledExecutor executor;
    private ReplayLogLoadService loaderService;

    /**
     * <p>
     *     Sets the test up
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception { // + File.separator + "145482863098"

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
    }

    @Test
    public void testGetNextReplayLog() throws Exception {

    }

    @Test
    public void testBufferedUntil() throws Exception {

    }

    @Test
    public void testLoadData() throws Exception {

    }

    @Test
    public void testGetData() throws Exception {

    }

    @Test
    public void testNoDataToLoad() throws Exception {
        File replayLogs = new File("." + File.separator + "src" + File.separator + "test" + File.separator
                + "resources" + File.separator + "data" + File.separator + "replaylogs-noloaddata");
        fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(invocation -> replayLogs.getParentFile());
        when(fileSystem.getReplayLogFolder()).thenAnswer(invocation -> replayLogs);

        executor = new LoggedScheduledExecutor(3);

        graphProxy = mock(GraphProxy.class);

        loaderService = new ReplayLogLoadService(executor, fileSystem, graphProxy);
    }
}