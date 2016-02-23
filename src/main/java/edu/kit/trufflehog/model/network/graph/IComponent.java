package edu.kit.trufflehog.model.network.graph;

import java.io.Serializable;

/**
 * Created by jan on 23.02.16.
 */
public interface IComponent extends Serializable {

    /**
     *
     * @return the name of this component
     */
    String name();

    boolean isMutable();

    IComponent createDeepCopy();

    /**
     * Updates this component with the given component
     * @param update the component to update this component
     * @return true if the update was successfull and values changed, false if there were no changes
     *          or f the component is immutable, thus cannot be updated
     */
    boolean update(IComponent update);
}
