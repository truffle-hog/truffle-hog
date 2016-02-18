package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.graph.IGraphProxy;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import edu.kit.trufflehog.util.IListener;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public final class ReplayLogController implements IReplayLogController {
    private final ReplayLogLoadService replayLogLoadService;
    private final ReplayLogSaveService replayLogSaveService;

    public ReplayLogController(FileSystem fileSystem, LoggedScheduledExecutor scheduledExecutor, IGraphProxy graphProxy,
                               INetworkGraph networkGraph) {
        replayLogSaveService = new ReplayLogSaveService(networkGraph, fileSystem, scheduledExecutor);
        replayLogLoadService = new ReplayLogLoadService(scheduledExecutor, fileSystem, graphProxy);
    }

    @Override
    public void startPlay(long instant, boolean strict) {
        replayLogLoadService.play(instant, strict);
    }

    @Override
    public void stopPlay() {
        replayLogLoadService.stop();
    }

    @Override
    public void jumpToInstant(long instant, boolean strict) {
        replayLogLoadService.jumpToInstant(instant, strict);
    }

    @Override
    public void startCapture() {
        replayLogSaveService.startCapture();
    }

    @Override
    public void stopCapture() {
        replayLogSaveService.stopCapture();
    }

    @Override
    public IListener<IReplayCommand> asListener() {
        return replayLogSaveService::receive;
    }

    @Override
    public void registerListener(IListener<IReplayCommand> listener) {
        replayLogLoadService.addListener(listener);
    }

    @Override
    public void unregisterListener(IListener<IReplayCommand> listener) {
        replayLogLoadService.removeListener(listener);
    }
}
