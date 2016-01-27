package de.fraunhofer.iosb.trufflehog.service.datalogging;

/**
 * <p>
 *     The DataLogSaveService saves the current graph state so that it can be fully reconstructed at a later date. It
 *     does this by creating {@link DataLog} objects at a fixed time interval (each DataLog object covers the data for
 *     this time interval). Thus the DataLogSaveService runs in its own thread.
 * </p>
 */
public class DataLogSaveService implements Runnable{

    /**
     * <p>
     *     Creates a new DataLogSaveService object.
     * </p>
     */
    public DataLogSaveService() {
    }

    /**
     * <p>
     *     Starts the data log save service.
     * </p>
     * <p>
     *     The DataLogSaveService saves the current graph state so that it can be fully reconstructed at a later date.
     *     It does this by creating {@link DataLog} objects at a fixed time interval (each DataLog object covers the
     *     data for this time interval). Thus the DataLogSaveService runs in its own thread.
     * </p>
     */
    @Override
    public void run() {
    }
}
