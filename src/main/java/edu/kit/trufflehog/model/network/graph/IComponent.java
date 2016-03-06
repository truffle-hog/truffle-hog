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
     * <p>
     *     Updated this component using the given node
     * </p>
     * @param node INode to update this component with
     * @return true if the update was successful, false if there were no changes or the component is immutable
     * and thus cannot be updated
     */
    boolean update(IComponent node);
}
