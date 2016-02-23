package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;

import java.io.Serializable;

/**
 * <p>
 *     Interface used to represent nodes in the graph.
 * </p>
 */
public interface INode extends Comparable<INode>, IComposition, Serializable {

    IAddress getAddress();

    INode createDeepCopy();

}
