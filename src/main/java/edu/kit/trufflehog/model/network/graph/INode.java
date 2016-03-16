package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;

import java.io.Serializable;

/**
 * <p>
 *     Interface used to represent nodes in the graph.
 * </p>
 */

public interface INode extends IComposition, Serializable {

    /**
     * @return the address of this Node
     */
    IAddress getAddress();

    /**
     * Updates this node with the given node
     * @param update the node that updates this node
     * @return true if the update was successful and values change, false otherwise
     *//*
    boolean update(INode update);*/

}
