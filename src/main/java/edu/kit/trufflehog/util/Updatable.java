package edu.kit.trufflehog.util;

import edu.kit.trufflehog.model.network.graph.IUpdater;

/**
 * This interface is used for Objects that want to be updatable by other objects of the same type with different/new values
 * and/or define some update logic that has to be performed when an instance gets updated.
 * @param <T> the type of the class to be updated
 *
 * @author Jan Hermes
 */
public interface Updatable<T> {

    /**
     * Updates this class with the given instance of the same class. The actual update logic can include any functionality
     * that is desired by the programmer. It may or may not include the actual values of the instance
     * @param instance the instance used for updating this instance
     * @return \code true \endcode if this instance was updated, \code false \endcode if there was no success in updating
     *              or no values changed
     */
    boolean update(T instance, IUpdater updater);
}
