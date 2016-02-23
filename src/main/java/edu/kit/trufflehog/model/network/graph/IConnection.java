package edu.kit.trufflehog.model.network.graph;

import java.io.Serializable;

/**
 * <p>
 *     Interface used to represent communication relations of nodes in the graph.
 * </p>
 */
public interface IConnection extends Comparable<IConnection>, IComposition, Serializable {

    INode getSrc();

    INode getDest();

    IConnection createDeepCopy();

}
