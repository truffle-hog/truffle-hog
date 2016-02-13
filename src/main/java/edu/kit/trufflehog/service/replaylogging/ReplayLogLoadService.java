package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import edu.kit.trufflehog.util.Notifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.MissingResourceException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * <p>
 *      The ReplayLogLoadService controls the playback of the playback graph (the old graph, generated from the
 *      {@code ReplayLog}s). It runs in its own thread, however most of its methods are called in other threads where
 *      the actual playback is controlled. Instead the ReplayLogLoadService makes sure there is always data to playback
 *      in the background by loading and buffering {@link ReplayLog} objects, and dispatching the
 *      commands of the ReplayLog objects. (The ReplayLogLoadService can do this since it is also a Notifier).
 * </p>
 */
public class ReplayLogLoadService extends Notifier<ICommand> {
    private static final Logger logger = LogManager.getLogger(ReplayLogLoadService.class);

    private ReplayLogLoader replayLogLoader;
    private ReplayLog currentReplayLog = null;

    private final ExecutorService executorService;
    private Future<?> future;

    private long startBuffering;

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
     *
     * </p>
     */
    private long START_BUFFER_X_REPLAY_LOGS_BEFORE;


    /**
     * <p>
     *     Creates a new ReplayLogLoadService object.
     * </p>
     */
    public ReplayLogLoadService(LoggedScheduledExecutor executor, FileSystem fileSystem) {
        this.executorService = executor;
        this.replayLogLoader = new ReplayLogLoader(fileSystem, MAX_BUFFER_SIZE, MAX_BATCH_SIZE, LOAD_TIME_LIMIT);

        MAX_BUFFER_SIZE = 200; //TODO: hook up with settings stuff
        MAX_BATCH_SIZE = 20;   //TODO: hook up with settings stuff
        LOAD_TIME_LIMIT = 3;   //TODO: hook up with settings stuff
        START_BUFFER_X_REPLAY_LOGS_BEFORE = 5; //TODO: hook up with settings stuff
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
     *
     * @param instant The instant in time from which to start playing
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     */
    public void play(Instant instant, boolean strict) {
        this.future = executorService.submit(() -> run(instant, strict));
    }

    /**
     * <p>
     *     Stops the playback of the {@link ReplayLog}s if it was in progress.
     * </p>
     */
    public void stop() {
        future.cancel(true);
    }

    /**
     * <p>
     *     The current playback of the {@link ReplayLog}s will jump to the location in time closest to the given instant,
     *     if the playback is currently active.
     * </p>
     *
     * @param instant The instant in time to which to jump in the playback, if it is active.
     */
    public void jumpToInstant(Instant instant) {
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
        boolean canLoad = true;

        //
        if (currentReplayLog == null && !load(instant, strict)) {
            logger.error("Stopped running because no replay logs were found");
            return;
        }

        long replayLogDuration = currentReplayLog.getEndInstant().toEpochMilli()
                - currentReplayLog.getStartInstant().toEpochMilli();

        //
        startBuffering = replayLogDuration * START_BUFFER_X_REPLAY_LOGS_BEFORE;

        while (!Thread.currentThread().isInterrupted()) {
            //
            if (currentReplayLog == null) {
                return;
            }

            //
            final long bufferedUntil = replayLogLoader.bufferedUntil();
            Future<Boolean> loadResult = null;
            if (bufferedUntil - currentReplayLog.getEndInstant().toEpochMilli() <= startBuffering && canLoad) {
                loadResult = executorService.submit(() ->
                        replayLogLoader.loadData(Instant.ofEpochMilli(bufferedUntil + (replayLogDuration / 2))));
            }

            //
            Future<ReplayLog> bufferResult = executorService.submit(() ->
                    replayLogLoader.getNextReplayLog(currentReplayLog));

            //
            currentReplayLog.getCommands().forEach((command, instantLocal) -> notifyListeners(command));

            //
            if (loadResult != null) {
                try {
                    canLoad = loadResult.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            //
            try {
                currentReplayLog = bufferResult.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
