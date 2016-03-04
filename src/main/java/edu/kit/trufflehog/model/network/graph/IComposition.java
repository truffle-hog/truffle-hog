package edu.kit.trufflehog.model.network.graph;

import java.util.Collection;

/**
 * This interface defines the basic methods for compositions. A composition representes a collection of Components.
 * @author Jan Hermes
 */
public interface IComposition extends IComponent, Collection<IComponent> {

    /**
     *
     * Adds the specified Component to this Composition.
     *
     * @param component the component
     * @param <T> Extending IComponent
     * @return {@code null} if there was no previous component of the same type. Otherwise returns the previous
     *          component, leaving it in the composition. Component can only be exchanged by explicitly removing
     *          and adding the new component of the same type.
     */
    <T extends IComponent> T addComponent(final T component);

    /**
     * Removes the component of the given type from this Composition.
     * @param type The type of Component to be removed
     * @param <T> Extending IComponent
     * @return The registered component if there was one, {@code null} otherwise.
     */
    <T extends IComponent> T removeComponent(final Class<T> type);

    /**
     * Retrieves the given component that is registered to this class type
     * @param componentType the type of component to be retrieved
     * @param <T> Extending IComponent
     * @return The component registered with the given type, \code null \endcode otherwise.
     */
    <T extends IComponent> T getComponent(final Class<T> componentType);

}
