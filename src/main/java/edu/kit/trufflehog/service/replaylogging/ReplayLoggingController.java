package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.graph.IGraphProxy;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import edu.kit.trufflehog.util.IListener;

import java.util.List;

/**
 * <p>
 *     The ReplayLoggingController is the implementation of the IReplayController. It serves as a simple facade to the whole
 *     package and gives access to the key methods that are available outside the package. Through it, the entire replay
 *     logging can be easily controlled.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public final class ReplayLoggingController implements IReplayLoggingController {
    private final ReplayLogLoadService replayLogLoadService;
    private final ReplayLogSaveService replayLogSaveService;

    /**
     * <p>
     *     Create a new ReplayLoggingController object.
     * </p>
     *
     * @param executorService The thread pool to which the replay logging threads will be submitted to
     * @param fileSystem The fileSystem object that gives access to the locations where files are saved on the hard drive.
     *                   This is necessary because of the ReplayLogger that needs to save ReplayLogs.
     * @param graphProxy The graph proxy object that is used to control what graph is being displayed
     * @param networkGraph The live graph so that the SnapshotLogger can take a snapshot of it.
     */
    public ReplayLoggingController(FileSystem fileSystem, LoggedScheduledExecutor executorService, IGraphProxy graphProxy,
                                   INetworkGraph networkGraph) {
        replayLogSaveService = new ReplayLogSaveService(networkGraph, fileSystem, executorService);
        replayLogLoadService = new ReplayLogLoadService(executorService, fileSystem, graphProxy);
    }

    @Override
    public void startPlay(long instant, boolean strict) {
        replayLogLoadService.play(instant, strict);
    }

    @Override
    public void startPlay(ICaptureSession captureSession, boolean strict) {
        replayLogLoadService.play(captureSession, strict);
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
    public List<ICaptureSession> getAllCaptureSessions() {
        return replayLogLoadService.getAllCaptureSessions();
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
