package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.graph.GraphProxy;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import edu.kit.trufflehog.util.Notifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.MissingResourceException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 *      The ReplayLogLoadService controls the playback of the playback graph (the old graph, generated from the
 *      {@code ReplayLog}s). It runs in its own thread, however most of its methods are called in other threads where
 *      the actual playback is controlled. Instead the ReplayLogLoadService makes sure there is always data to playback
 *      in the background by loading and buffering {@link ReplayLog} objects, and dispatching the
 *      commands of the ReplayLog objects. (The ReplayLogLoadService can do this since it is also a Notifier).
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class ReplayLogLoadService extends Notifier<IReplayCommand> {
    private static final Logger logger = LogManager.getLogger(ReplayLogLoadService.class);

    private final ReplayLogLoader replayLogLoader;
    private ReplayLog currentReplayLog = null;
    private final GraphProxy graphProxy;

    private final ExecutorService executorService;
    private Future<?> loadServiceFuture;
    private boolean isPlaying;

    /**
     * <p>
     *     The maximum number of data logs that can be loaded into memory
     * </p>
     */
    private int MAX_BUFFER_SIZE;

    /**
     * <p>
     *     The maximum number of data logs to deserialize at once
     * </p>
     */
    private int MAX_BATCH_SIZE;

    /**
     * <p>
     *     The maximum number of seconds a replay log's covered interval can deviate from the desired instant
     * </p>
     */
    private int LOAD_TIME_LIMIT;

    /**
     * <p>
     *     Sets the number of {@link ReplayLog}s away from the end of buffered content that new replay logs will be
     *     loaded into the buffer.
     * </p>
     * <p>
     *      So if say this variable is set to 3, and there are 10 replay logs in the buffer, the new batch of replay logs
     *      will be loaded at replay log no. 7 (10 - 3 = 7)
     * </p>
     */
    private long START_BUFFER_X_REPLAY_LOGS_BEFORE;


    /**
     * <p>
     *     Creates a new ReplayLogLoadService object.
     * </p>
     */
    public ReplayLogLoadService(LoggedScheduledExecutor executor, FileSystem fileSystem, GraphProxy graphProxy) {
        MAX_BUFFER_SIZE = 200; //TODO: hook up with settings stuff
        MAX_BATCH_SIZE = 20;   //TODO: hook up with settings stuff
        LOAD_TIME_LIMIT = 3;   //TODO: hook up with settings stuff
        START_BUFFER_X_REPLAY_LOGS_BEFORE = 5; //TODO: hook up with settings stuff

        this.executorService = executor;
        this.replayLogLoader = new ReplayLogLoader(fileSystem, MAX_BUFFER_SIZE, MAX_BATCH_SIZE, LOAD_TIME_LIMIT);
        this.graphProxy = graphProxy;
        isPlaying = false;
    }

    /**
     * <p>
     *     Starts the playback of the {@link ReplayLog}s. The graph snapshot will be loaded and the subsequent commands
     *     will be executed on that snapshot.
     * </p>
     * <p>
     *     If strict is set, a replay log containing the given time instant must be returned, else false is returned.
     *     If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else false is returned.
     * </p>
     *
     * @param instant The instant in time from which to start playing
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     */
    public void play(Instant instant, boolean strict) {
        if (!isPlaying) {
            this.loadServiceFuture = executorService.submit(() -> run(instant, strict));
            isPlaying = true;
            logger.debug("Now playing replay logs from: " + instant);
        } else {
            logger.debug("Cannot play, already playing.");
        }
    }

    /**
     * <p>
     *     Stops the playback of the {@link ReplayLog}s if it was in progress.
     * </p>
     */
    public void stop() {
        if (isPlaying) {
            loadServiceFuture.cancel(true);

            // Sleep until the thread was terminated successfully
            while (!loadServiceFuture.isDone()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    logger.error("Unable to wait for replay log load service thread to finish", e);
                }
            }

            isPlaying = false;
            logger.debug("Stopped playing replay logs.");
        } else {
            logger.debug("Cannot stop, already stopped.");
        }
    }

    /**
     * <p>
     *     The current playback of the {@link ReplayLog}s will jump to the location in time closest to the given instant,
     *     if the playback is currently active.
     * </p>
     * <p>
     *     If strict is set, a replay log containing the given time instant must be returned, else false is returned.
     *     If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else false is returned.
     * </p>
     *
     * @param instant The instant in time to which to jump in the playback, if it is active.
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     */
    public void jumpToInstant(Instant instant, boolean strict) {
        stop();

        // Start it again!
        play(instant, strict);
    }

    /**
     * <p>
     *     Loads a batch of {@link ReplayLog}s closest to the given time instant into memory so that they are ready when
     *     needed. It does this through the {@link ReplayLogLoader}.
     * </p>
     * <p>
     *     If strict is set, a replay log containing the given time instant must be returned, else false is returned.
     *     If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else false is returned.
     * </p>
     *
     * @param instant The time instant that should be used to load the ReplayLog objects. The ReplayLog objects closest to
     *                the time instant will be loaded.
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     * @return True if a replay log was found and loaded, else false
     */
    private boolean load(Instant instant, boolean strict) {
        try {
            currentReplayLog = replayLogLoader.getData(instant, strict);
            return true;
        } catch (MissingResourceException e) {
            logger.error("No replay logs buffered that match given instant, loading new replay logs from the hard drive");
            if (replayLogLoader.loadData(instant)) {
                logger.debug("Loaded replay logs from hard drive, returning found replay log");
                currentReplayLog = replayLogLoader.getData(instant, strict);
                return true;
            } else {
                logger.error("No replay logs found on the hard drive.");
            }
        }

        return false;
    }

    /**
     * <p>
     *     Starts the replay log load service.
     * </p>
     * <p>
     *     The ReplayLogLoadService runs in its own thread to buffer {@link ReplayLog} objects in the background so that
     *     when the graph playback function is activated, the old graph can immediately be started. It also notifies the
     *     playback executor with the old commands found in the ReplayLog objects so that the executor can update the
     *     playback graph.
     * </p>
     * <p>
     *     If strict is set, a replay log containing the given time instant must be returned, else false is returned.
     *     If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else false is returned.
     * </p>
     *
     * @param instant The instant in time from which to start playing
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     */
    public void run(Instant instant, boolean strict) {
        final AtomicBoolean canLoad = new AtomicBoolean(true);

        // Load the very first replay log. If none is found, stop right here
        if (currentReplayLog == null && !load(instant, strict)) {
            logger.error("Stopped running because no replay logs were found");
            stop();
            return;
        }

        // Get the duration of one replay log for this instance (all replay logs in the same folder should have the
        // same duration)
        long replayLogDuration = currentReplayLog.getEndInstant()
                - currentReplayLog.getStartInstant();

        // Determine at how many milliseconds before the end of the buffered data we should start loading new data into
        // the buffer
        long startBuffering = replayLogDuration * START_BUFFER_X_REPLAY_LOGS_BEFORE;

        // Set this graph as the graph to display
        graphProxy.setActive(currentReplayLog.getGraphSnapshot());

        Future loadResult = null;
        Future<ReplayLog> bufferResult = null;
        while (!Thread.currentThread().isInterrupted()) {
            // Determine whether a new thread should be started which would load new data from the hard drive into
            // memory. This submission into the thread pool will return a future which will be used later on to sync
            // both threads again.
            final long bufferedUntil = replayLogLoader.bufferedUntil();
            if (bufferedUntil - currentReplayLog.getEndInstant() <= startBuffering && canLoad.get()) {
                loadResult = executorService.submit(() -> canLoad.set(replayLogLoader.
                        loadData(Instant.ofEpochMilli(bufferedUntil + (replayLogDuration / 2)))));
            }

            // Here we create a new thread to already fetch the next replay log from the buffer
            bufferResult = executorService.submit(() ->
                    replayLogLoader.getNextReplayLog(currentReplayLog));

            // This is the actual purpose of the thread - send the old commands out to all listeners via the notifier
            currentReplayLog.getCommands().forEach((command, timeUntilNextCommand) -> {
                notifyListeners(command);

                // Sleep for the same duration that the
                try {
                    Thread.sleep(timeUntilNextCommand);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            // Now wait until the next replay log has been retrieved from the buffer
            ReplayLog replayLogTemp = null;
            try {
                replayLogTemp = bufferResult.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Unable to wait for next replay log from buffer", e);
            }

            // Check if the retrieved replay log is null or not, if it is then wait for the loading of new replay logs
            // from the hard drive if that is still in process. Otherwise there is nothing more the play and we are done
            if (replayLogTemp != null) {
                currentReplayLog = replayLogTemp;
            } else {
                // If we did load new replay logs into the buffer, wait for them to finish loading and get the next replay
                // log after the last one
                if (loadResult != null) {
                    try {
                        // Wait until loaded from hard drive
                        loadResult.get();

                        // If the loading operation was successful, canLoad will be true and we can get the next
                        // replay log, if every thing is in order
                        if (canLoad.get()) {
                            currentReplayLog = replayLogLoader.getNextReplayLog(currentReplayLog);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("Unable to wait for next replay log from buffer", e);
                    }
                }
            }

            // If the loading operation was not successful, or if it did load some replay logs but not the next one,
            // then we cannot continue and quit
            if (currentReplayLog == null) {
                stop();
                return;
            }
        }

        // Cancel all other threads that might be running after the interrupt on this thread
        if (loadResult != null) {
            loadResult.cancel(true);
        }
        if (bufferResult != null) {
            bufferResult.cancel(true);
        }
    }
}
