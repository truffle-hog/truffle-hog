package edu.kit.trufflehog.util;

/**
 * This interface is used for Objects that want to be updatable by other objects of the same type with different/new values.
 * @param <T> the type of the class to be updated
 */
public interface Updatable<T> extends Mutability {

    /**
     * Updates this class with the given instance of the same class.
     * @param instance the instance used for updating this instance
     * @return \code true \endcode if this instance was updated, \code false \endcode if there was no success in updating
     *              or no values changed
     */
    boolean update(T instance);
}
