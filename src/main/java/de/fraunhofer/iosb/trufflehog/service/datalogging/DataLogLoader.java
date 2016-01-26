package de.fraunhofer.iosb.trufflehog.service.datalogging;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Instant;
import java.util.MissingResourceException;

/**
 * <p>
 *     The DataLogLoader loads {@link DataLog}s from the hard drive into memory, so that when the view needs them to
 *     display the old graph, it is at the view's disposal.
 * </p>
 */
public class DataLogLoader {

    /**
     * <p>
     *     Creates a new DataLogLoader object.
     * </p>
     */
    public DataLogLoader() {
    }

    /**
     * <p>
     *     Loads a batch of {@link DataLog}s closest to the given time instant into memory so that they are ready when
     *     needed.
     * </p>
     * <p>
     *     This method should not be confused with {@link #getData(Instant)}. {@link #getData(Instant)} gets
     *     the desired DataLog object from the already loaded DataLog objects and does not load any DataLog itself.
     * </p>
     * @param instant The time instant that should be used to load the DataLog objects. The DataLog objects closest to
     *                the time instant will be loaded.
     */
    public void loadData(Instant instant) {
    }

    /**
     * <p>
     *      Gets the DataLog object closest to the given time instant.
     * </p>
     * <p>
     *      Condition: A DataLog reasonably close in time to the given instant must be in memory, else a
     *      MissingResourceException is thrown.
     * </p>
     *
     * @param instant The time instant that should be used to get the DataLog object. The DataLog object closest to
     *                the time instant will be returned.
     * @return The DataLog object closest in time to the given time instant.
     * @throws MissingResourceException Thrown when no DataLog reasonably close in time to the given instant is found
     *                                  in memory.
     */
    public DataLog getData(Instant instant) throws MissingResourceException {
        throw new NotImplementedException();
    }
}
