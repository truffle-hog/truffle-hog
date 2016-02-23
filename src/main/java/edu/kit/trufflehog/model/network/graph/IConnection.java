package edu.kit.trufflehog.model.network.graph;

/**
 * <p>
 *     Interface used to represent communication relations of nodes in the graph.
 * </p>
 */
public interface IConnection extends Comparable<IConnection> {

    INode getSrc();

    INode getDest();
}
