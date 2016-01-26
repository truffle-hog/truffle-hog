package de.fraunhofer.iosb.trufflehog.service.datalogging;

import de.fraunhofer.iosb.trufflehog.command.ICommand;
import de.fraunhofer.iosb.trufflehog.communication.Notifier;

import java.time.Instant;

/**
 * <p>
 *      The DataLogLoadService controls the playback of the playback graph (the old graph, generated from the
 *      {@code DataLog}s). It runs in its own thread, however most of its methods are called in other threads where
 *      the actual playback is controlled. Instead the DataLogLoadService makes sure there is always data to playback
 *      in the background by loading and buffering {@link DataLog} objects, and dispatching the
 *      commands of the DataLog objects. (The DataLogLoadService can do this since it is also a Notifier).
 * </p>
 */
public class DataLogLoadService extends Notifier<ICommand> implements Runnable {

    /**
     * <p>
     *     Creates a new DataLogLoadService object.
     * </p>
     */
    public DataLogLoadService() {
    }

    /**
     * <p>
     *     Starts the playback of the {@link DataLog}s. The graph snapshot will be loaded and the subsequent commands
     *     will be executed on that snapshot.
     * </p>
     */
    public void play() {
    }

    /**
     * <p>
     *     Pauses the playback of the {@link DataLog}s if it was in progress.
     * </p>
     */
    public void pause() {
    }

    /**
     * <p>
     *     The current playback of the {@link DataLog}s will jump to the location in time closest to the given instant,
     *     if the playback is currently active.
     * </p>
     * @param instant The instant in time to which to jump in the playback, if it is active.
     */
    public void jumpToInstant(Instant instant) {
    }

    /**
     * <p>
     *     Loads a batch of {@link DataLog}s closest to the given time instant into memory so that they are ready when
     *     needed.
     * </p>
     * @param instant The time instant that should be used to load the DataLog objects. The DataLog objects closest to
     *                the time instant will be loaded.
     */
    public void load(Instant instant) {

    }

    /**
     * <p>
     *     The DataLogLoadService runs in its own thread to buffer {@link DataLog} objects in the background so that
     *     when the graph playback function is activated, the old graph can immediately be started. It also notifies the
     *     playback executor with the old commands found in the DataLog objects so that the executor can update the
     *     playback graph.
     * </p>
     */
    @Override
    public void run() {

    }
}
