package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.model.graph.AbstractNetworkGraph;
import edu.kit.trufflehog.model.graph.GraphProxy;
import edu.kit.trufflehog.util.IListener;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * <p>
 *     The ReplayLogSaveService saves the current graph state so that it can be fully reconstructed at a later date. It
 *     does this by creating {@link ReplayLog} objects at a fixed time interval (each ReplayLog object covers the data for
 *     this time interval). Thus the ReplayLogSaveService runs in its own thread.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ReplayLogSaveService implements IListener<ICommand>, Runnable {
    private CommandLogger commandLogger;
    private SnapshotLogger snapshotLogger;
    private ReplayLogger replayLogger;

    private LoggedScheduledExecutor executorService;
    private int createReplayLogInterval;
    private Lock lock;

    /**
     * <p>
     *     Creates a new ReplayLogSaveService object.
     * </p>
     *
     *param graphProxy The proxy object that contains the graph so that the SnapshotLogger can take a snapshot of it.xy
     */
    public ReplayLogSaveService(GraphProxy graphProxy) {
        // Create all the logging objects
        commandLogger = new CommandLogger();
        snapshotLogger = new SnapshotLogger(graphProxy);
        replayLogger = new ReplayLogger();

        // Instantiate the scheduled executor service
        executorService = new LoggedScheduledExecutor(1);
        lock = new ReentrantLock();

        // TODO: Think about whether this is good
        ScheduledFuture<?> replayLogExecutor = executorService.scheduleAtFixedRate(this, 0, createReplayLogInterval, SECONDS);
    }

    /**
     * <p>
     *     Starts the replay log save service.
     * </p>
     * <p>
     *     The ReplayLogSaveService saves the current graph state so that it can be fully reconstructed at a later date.
     *     It does this by creating {@link ReplayLog} objects at a fixed time interval (each ReplayLog object covers the
     *     data for this time interval). Thus the ReplayLogSaveService runs in its own thread.
     * </p>
     */
    @Override
    public void run() {
        // Copy all commands that were received until now into the temporary list with a lock, so that no commands are
        // added to the list while the list is being transferred
        lock.lock();
        commandLogger.transferList();
        lock.unlock();

        // Create a new replay log
        List<ICommand> compressedCommandList = commandLogger.createCommandLog();
        AbstractNetworkGraph graph = snapshotLogger.takeSnapshot();
        ReplayLog replayLog = replayLogger.createReplayLog(graph, compressedCommandList);

        // Save the replay log to the disk
        replayLogger.saveReplayLog(replayLog);
    }

    /**
     * <p>
     *     Through this method the ReplayLogSaveService receives new Commands that it should save into the list so that
     *     they can be turned into {@link ReplayLog}s later.
     * </p>
     *
     * @param command The command to save
     */
    @Override
    public void receive(ICommand command) {
        // Lock so that the list cannot be transferred while a command is being added to it. See the run method for more
        lock.lock();
        commandLogger.addCommand(command);
        lock.unlock();
    }
}
