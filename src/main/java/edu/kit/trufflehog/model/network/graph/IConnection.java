package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.util.ICopyCreator;

import java.io.Serializable;

/**
 * <p>
 *     Interface used to represent communication relations of node in the graph.
 * </p>
 */
public interface IConnection extends IComposition, Serializable {

    INode getSrc();

    INode getDest();

    @Override
    IConnection createDeepCopy(ICopyCreator copyCreator);


 //   boolean update(IConnection update);

}
