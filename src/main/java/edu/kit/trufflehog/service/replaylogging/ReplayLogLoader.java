package edu.kit.trufflehog.service.replaylogging;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Instant;
import java.util.MissingResourceException;

/**
 * <p>
 *     The ReplayLogLoader loads {@link ReplayLog}s from the hard drive into memory, so that when the view needs them to
 *     display the old graph, it is at the view's disposal.
 * </p>
 */
public class ReplayLogLoader {

    /**
     * <p>
     *     Creates a new ReplayLogLoader object.
     * </p>
     */
    public ReplayLogLoader() {
    }

    /**
     * <p>
     *     Loads a batch of {@link ReplayLog}s closest to the given time instant into memory so that they are ready when
     *     needed.
     * </p>
     * <p>
     *     This method should not be confused with {@link #getData(Instant)}. {@link #getData(Instant)} gets
     *     the desired ReplayLog object from the already loaded ReplayLog objects and does not load any ReplayLog itself.
     * </p>
     *
     * @param instant The time instant that should be used to load the ReplayLog objects. The ReplayLog objects closest to
     *                the time instant will be loaded.
     */
    public void loadData(Instant instant) {
    }

    /**
     * <p>
     *      Gets the ReplayLog object closest to the given time instant.
     * </p>
     * <p>
     *      Condition: A ReplayLog reasonably close in time to the given instant must be in memory, else a
     *      MissingResourceException is thrown.
     * </p>
     *
     * @param instant The time instant that should be used to get the ReplayLog object. The ReplayLog object closest to
     *                the time instant will be returned.
     * @return The ReplayLog object closest in time to the given time instant.
     * @throws MissingResourceException Thrown when no ReplayLog reasonably close in time to the given instant is found
     *                                  in memory.
     */
    public ReplayLog getData(Instant instant) throws MissingResourceException {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
