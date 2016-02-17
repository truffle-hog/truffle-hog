package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.graph.IGraphProxy;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import edu.kit.trufflehog.util.IListener;

import java.time.Instant;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public final class ReplayLogController {
    private ReplayLogLoadService replayLogLoadService;
    private ReplayLogSaveService replayLogSaveService;

    public ReplayLogController(FileSystem fileSystem, LoggedScheduledExecutor scheduledExecutor, IGraphProxy graphProxy,
                               INetworkGraph networkGraph) {
        replayLogSaveService = new ReplayLogSaveService(networkGraph, fileSystem, scheduledExecutor);
        replayLogLoadService = new ReplayLogLoadService(scheduledExecutor, fileSystem, graphProxy);
    }

    public void startPlay(Instant instant, boolean strict) {
        replayLogLoadService.play(instant, strict);
    }

    public void stopPlay() {
        replayLogLoadService.stop();
    }

    public void jumpToInstant(Instant instant, boolean strict) {
        replayLogLoadService.jumpToInstant(instant, strict);
    }

    public void startCapture() {
        replayLogSaveService.startCapture();
    }

    public void stopCapture() {
        replayLogSaveService.stopCapture();
    }

    public IListener<IReplayCommand> asListener() {
        return t -> replayLogSaveService.receive(t);
    }

    public void registerListener(IListener<IReplayCommand> listener) {
        replayLogLoadService.addListener(listener);
    }

    public void unregisterListener(IListener<IReplayCommand> listener) {
        replayLogLoadService.removeListener(listener);
    }
}
