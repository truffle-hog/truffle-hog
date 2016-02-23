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
}
