package edu.kit.trufflehog.model.network.graph;

import java.io.Serializable;

/**
 * <p>
 *     Interface used to represent communication relations of node in the graph.
 * </p>
 */
public interface IConnection extends Serializable {

    INode getSrc();

    INode getDest();

    IConnection createDeepCopy();

    /**
     * Updates this connection with the given connection
     * @param update the connection that updates this connection
     * @return true if this connection was updated, false if there was no success in updating
     *              or no values changes
     */
    boolean update(IConnection update);

    IComposition getComposition();

}
