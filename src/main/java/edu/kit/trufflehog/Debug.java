package edu.kit.trufflehog;


import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.graph.GraphProxy;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import edu.kit.trufflehog.service.replaylogging.ReplayLoggingController;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by julianbrendl on 2/16/16.
 */
public class Debug {
    private FileSystem fileSystem;
    private GraphProxy graphProxy;
    private LoggedScheduledExecutor executor;
    private ReplayLoggingController loggingController;

    public static void main(String[] args) {
        new Debug();
    }

    public Debug() {
        File replayLogs = new File("." + File.separator + "src" + File.separator + "test" + File.separator
                + "resources" + File.separator + "data" + File.separator + "replaylogs");
        fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(invocation -> replayLogs.getParentFile());
        when(fileSystem.getReplayLogFolder()).thenAnswer(invocation -> replayLogs);

        executor = new LoggedScheduledExecutor(3);

        graphProxy = mock(GraphProxy.class);

        loggingController = new ReplayLoggingController(fileSystem, executor, graphProxy, null);
        loggingController.startPlay(1455788579688L, false);
    }
}
