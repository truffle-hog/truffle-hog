package edu.kit.trufflehog.service.replaylogging;

/**
 * <p>
 *     The ReplayLogSaveService saves the current graph state so that it can be fully reconstructed at a later date. It
 *     does this by creating {@link ReplayLog} objects at a fixed time interval (each ReplayLog object covers the data for
 *     this time interval). Thus the ReplayLogSaveService runs in its own thread.
 * </p>
 */
public class ReplayLogSaveService implements Runnable {

    /**
     * <p>
     *     Creates a new ReplayLogSaveService object.
     * </p>
     */
    public ReplayLogSaveService() {
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
    }
}
