package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;

import java.io.Serializable;

/**
 * <p>
 *     Interface used to represent node in the graph.
 * </p>
 */
public interface INode extends IComposition, Serializable {

    IAddress getAddress();

    INode createDeepCopy();

    /**
     * Updates this node with the given node
     * @param update the node that updates this node
     * @return true if the update was successful and values changes, false otherwise
     */
    boolean update(INode update);

}
