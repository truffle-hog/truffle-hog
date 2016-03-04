package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.util.DeepCopyable;
import edu.kit.trufflehog.util.Updatable;

import java.io.Serializable;

/**
 * Created by jan on 23.02.16.
 */
public interface IComponent extends DeepCopyable<IComponent>, Updatable<IComponent>, Serializable {

    /**
     * @return the name of this component
     */
    String name();

}
