package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.util.ICopyCreator;

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
     * Creates a copy of this INode while also copying all of the references that are mutable. Immutable references
     * are just included by their references, as the cannot change through the lifetime of the program.
     * @return A deep copy of this INode
     */
    @Override
    INode createDeepCopy(ICopyCreator copyCreator);

    /**
     * Updates this node with the given node
     * @param update the node that updates this node
     * @return true if the update was successful and values change, false otherwise
     *//*
    boolean update(INode update);*/

}
