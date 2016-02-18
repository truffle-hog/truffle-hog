package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.util.IListener;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

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
class ReplayLogSaveService implements IListener<IReplayCommand>, Runnable {
    private static final Logger logger = LogManager.getLogger(ReplayLogSaveService.class);

    private final CommandLogger commandLogger;
    private final ReplayLogger replayLogger;
    private Instant startInstant;
    private final FileSystem fileSystem;
    private CaptureSession currentSession;
    private final INetworkGraph graph;

    private ScheduledFuture<?> saveServiceFuture;
    private final ScheduledExecutorService executorService;
    private boolean recording = false;

    /**
     * <p>
     *     The time interval between two replay logs (so how long a replay log should be at most)
     * </p>
     */
    private int LOGGING_INTERVAL;

    /**
     * <p>
     *     Creates a new ReplayLogSaveService object.
     * </p>
     *
     * @param graph The live graph so that the SnapshotLogger can take a snapshot of it.
     * @param fileSystem The fileSystem object that gives access to the locations where files are saved on the hard drive.
     *                   This is necessary because of the ReplayLogger that needs to save ReplayLogs.
     */
    public ReplayLogSaveService(INetworkGraph graph, FileSystem fileSystem, ScheduledExecutorService executorService) {
        this.commandLogger = new CommandLogger();
        this.replayLogger = new ReplayLogger();
        this.fileSystem = fileSystem;
        this.currentSession = null;
        this.graph = graph;

        this.executorService = executorService;
        LOGGING_INTERVAL = 500;    //TODO: hook up with settings stuff
    }

    /**
     * <p>
     *     Starts the replay log saving functionality of TruffleHog.
     * </p>
     * <p>
     *     This creates a new folder in the replaylog folder into which all recorded {@link ReplayLog}s will be saved.
     * </p>
     */
    public void startCapture() {
        if (recording) {
            logger.debug("Already recording replay logs.");
            return;
        }

        // Lock here because instant could be used in the thread calling the receive method
        synchronized (this) {
            startInstant = Instant.now();
            File currentReplayLogFolder = new File(fileSystem.getReplayLogFolder() + File.separator +
                    startInstant.toEpochMilli());
            currentSession = new CaptureSession(currentReplayLogFolder, startInstant);
        }

        if (!currentSession.create()) {
            logger.error("Unable to start a new capture session");
        }

        saveServiceFuture = executorService.scheduleAtFixedRate(this, LOGGING_INTERVAL, LOGGING_INTERVAL, MILLISECONDS);
        recording = true;
        logger.debug("Recording replay logs..");
    }

    /**
     * <p>
     *     Stops the replay log saving functionality of TruffleHog.
     * </p>
     */
    public void stopCapture() {
        if (!recording) {
            logger.debug("Already stopped recording replay logs.");
            return;
        }

        saveServiceFuture.cancel(true);

        // Lock here because instant could be used in the thread calling the receive method
        synchronized (this) {
            startInstant = null;
        }

        // Sleep until the thread was terminated successfully
        while (!saveServiceFuture.isDone()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logger.error("Unable to wait for replay log load service thread to finish", e);
            }
        }

        // End the current capture session, this renames the folder so that the folders name becomes
        if (currentSession.finish()) {
            logger.debug("Ended current capture session successfully");
        } else {
            logger.error("Unable to end current capture session successfully");
        }
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
        synchronized (this) {
            commandLogger.swapMaps();
        }

        // Create a new replay log
        LinkedMap<Long, IReplayCommand> compressedCommandList = commandLogger.createCommandLog();
        ReplayLog replayLog = replayLogger.createReplayLog(graph, compressedCommandList);

        // Save the replay log to the disk
        replayLogger.saveReplayLog(replayLog, currentSession);
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
    public synchronized void receive(IReplayCommand command) {
        // Lock so that the list cannot be transferred while a command is being added to it. See the run method for more
        // Drop the command if we are currently not recording
        if (startInstant == null) {
            return;
        }
        commandLogger.addCommand(command, startInstant);
    }
}
