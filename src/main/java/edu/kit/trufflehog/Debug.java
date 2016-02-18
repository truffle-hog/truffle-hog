package edu.kit.trufflehog;


import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.graph.GraphProxy;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import edu.kit.trufflehog.service.replaylogging.ReplayLogLoadService;

import java.io.File;
import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by julianbrendl on 2/16/16.
 */
public class Debug {
    private FileSystem fileSystem;
    private GraphProxy graphProxy;
    private LoggedScheduledExecutor executor;
    private ReplayLogLoadService loaderService;

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

        loaderService = new ReplayLogLoadService(executor, fileSystem, graphProxy);
        loaderService.play(Instant.ofEpochMilli(1455736020080L), false);
    }
}
