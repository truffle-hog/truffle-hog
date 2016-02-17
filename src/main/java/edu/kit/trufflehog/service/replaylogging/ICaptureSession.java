package edu.kit.trufflehog.service.replaylogging;

import java.io.File;
import java.time.Instant;
import java.util.Map;

/**
 * <p>
 *     The CaptureSession is the abstraction of a folder in which replay logs are contained. It provides
 *     functionality that is shared among many classes of the replaylogging package. For instance, it can retrieve the
 *     session's start and end times, the contained replay logs as a sorted list, etc.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface ICaptureSession {
    /**
     * <p>
     *     Gets the parent folder of all replay logs in this capture session.
     * </p>
     *
     * @return The parent folder of all replay logs in this capture session.
     */
    File getSessionFolder();

    /**
     * <p>
     *     Gets the instant the capture was started.
     * </p>
     *
     * @return The instant the capture was started.
     */
    Instant getStartInstant();

    /**
     * <p>
     *     Gets the instant the capture ended.
     * </p>
     *
     * @return The instant the capture ended.
     */
    Instant getEndInstant();

    /**
     * <p>
     *     Gets all replay log files sorted in ascending order based on their ending times.
     * </p>
     *
     * @return All replay log files sorted in ascending order based on their ending times.
     */
    Map<Long, File> getSortedReplayLogs();
}
