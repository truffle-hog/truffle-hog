package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.util.Notifier;

import java.time.Instant;

/**
 * <p>
 *      The ReplayLogLoadService controls the playback of the playback graph (the old graph, generated from the
 *      {@code ReplayLog}s). It runs in its own thread, however most of its methods are called in other threads where
 *      the actual playback is controlled. Instead the ReplayLogLoadService makes sure there is always data to playback
 *      in the background by loading and buffering {@link ReplayLog} objects, and dispatching the
 *      commands of the ReplayLog objects. (The ReplayLogLoadService can do this since it is also a Notifier).
 * </p>
 */
public class ReplayLogLoadService extends Notifier<ICommand> implements Runnable {

    /**
     * <p>
     *     Creates a new ReplayLogLoadService object.
     * </p>
     */
    public ReplayLogLoadService() {
    }

    /**
     * <p>
     *     Starts the playback of the {@link ReplayLog}s. The graph snapshot will be loaded and the subsequent commands
     *     will be executed on that snapshot.
     * </p>
     */
    public void play() {
    }

    /**
     * <p>
     *     Pauses the playback of the {@link ReplayLog}s if it was in progress.
     * </p>
     */
    public void pause() {
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
     *
     * @param instant The time instant that should be used to load the ReplayLog objects. The ReplayLog objects closest to
     *                the time instant will be loaded.
     */
    public void load(Instant instant) {
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
     */
    @Override
    public void run() {
    }
}
