package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.util.IListener;

import java.util.List;

/**
 * <p>
 *     The IReplayLoggingController is the abstraction from the whole replay logging service. It serves as a simple facade
 *     to the whole package and gives access to the key methods that are available outside the package. Through it, the
 *     entire replay logging can be easily controlled.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface IReplayLoggingController {

    /**
     * <p>
     *     Starts the playback of the {@link ReplayLog}s. The graph snapshot will be loaded and the subsequent commands
     *     will be executed on that snapshot.
     * </p>
     * <p>
     *     If strict is true, a replay log containing the given time instant must be returned, else false is returned.
     *     If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else false is returned.
     * </p>
     * <p>
     *     This is done because there is a small gap in time between different replay logs, usually between 25-50 milliseconds
     *     because the logger needs time to execute as well. If the user happens to be unlucky to select such a gap as
     *     the next replay log that should be played, than the system does not crash. However since this does deviate
     *     from the expected behaviour it is made optional.
     * </p>
     *
     * @param instant The instant in time from which to start playing
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     */
    void startPlay(long instant, boolean strict);

    /**
     * <p>
     *     Starts the playback of the {@link ReplayLog}s. The graph snapshot will be loaded and the subsequent commands
     *     will be executed on that snapshot.
     * </p>
     * <p>
     *     If strict is true, a replay log containing the given time instant must be returned, else false is returned.
     *     If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else false is returned.
     * </p>
     * <p>
     *     This is done because there is a small gap in time between different replay logs, usually between 25-50 milliseconds
     *     because the logger needs time to execute as well. If the user happens to be unlucky to select such a gap as
     *     the next replay log that should be played, than the system does not crash. However since this does deviate
     *     from the expected behaviour it is made optional.
     * </p>
     *
     * @param captureSession The {@link ICaptureSession} to playback
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     */
    void startPlay(ICaptureSession captureSession, boolean strict);

    /**
     * <p>
     *     Stops the playback of the {@link ReplayLog}s if it was in progress.
     * </p>
     */
    void stopPlay();

    /**
     * <p>
     *     The current playback of the {@link ReplayLog}s will jump to the location in time closest to the given instant,
     *     if the playback is currently active.
     * </p>
     * <p>
     *     If strict is true, a replay log containing the given time instant must be returned, else false is returned.
     *     If strict is false, a ReplayLog reasonably close in time to the given
     *     instant must be in memory, else false is returned.
     * </p>
     * <p>
     *     This is done because there is a small gap in time between different replay logs, usually between 25-50 milliseconds
     *     because the logger needs time to execute as well. If the user happens to be unlucky to select such a gap as
     *     the next replay log that should be played, than the system does not crash. However since this does deviate
     *     from the expected behaviour it is made optional.
     * </p>
     *
     * @param instant The instant in time to which to jump in the playback, if it is active.
     * @param strict If strict is set to true, than a replay log will only be returned if it strictly lies in the
     *               covered time interval of the replay log. If it is set to false, then a replay log will even be
     *               returned if the time instant lies in +- {@link ReplayLogLoadService#LOAD_TIME_LIMIT} of its covered
     *               interval.
     */
    void jumpToInstant(long instant, boolean strict);

    /**
     * <p>
     *     Gets all {@link ICaptureSession}s found on the hard drive, or null if none were found.
     * </p>
     *
     * @return All {@link ICaptureSession}s found on the hard drive, or null if none were found.
     */
    List<ICaptureSession> getAllCaptureSessions();

    /**
     * <p>
     *     Starts the replay log saving functionality of TruffleHog.
     * </p>
     * <p>
     *     This creates a new folder in the replaylog folder into which all recorded {@link ReplayLog}s will be saved.
     * </p>
     */
    void startCapture();

    /**
     * <p>
     *     Stops the replay log saving functionality of TruffleHog which among other things renames the created folder
     *     to the start time of the first replay log dash the stop time of the last replay log. (E.g. 131234234234 ->
     *     131234234250-131234238593)
     * </p>
     */
    void stopCapture();

    /**
     * <p>
     *     Returns the listener of the {@link ReplayLogSaveService} so that it can log its IReplayCommands
     * </p>
     *
     * @return the listener of the {@link ReplayLogSaveService}
     */
    IListener<IReplayCommand> asListener();

    /**
     * <p>
     *     Registers a listener with the {@link ReplayLogLoadService} so that it gets the commands sent from the loading
     *     service.
     * </p>
     *
     * @param listener The listener to be registered with the {@link ReplayLogLoadService} so that it gets the commands
     *                 sent to it
     */
    void registerListener(IListener<IReplayCommand> listener);

    /**
     * <p>
     *     Unregisters a listener with the {@link ReplayLogLoadService} so that it no longer gets commands sent to it
     *     from the loading service.
     * </p>
     *
     * @param listener The listener to be unregistered from the {@link ReplayLogLoadService}
     */
    void unregisterListener(IListener<IReplayCommand> listener);
}
