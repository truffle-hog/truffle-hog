package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;

/**
 * <p>
 *     Creates a {@link Layout} implementation of the Kamada-Kawai algorithm.
 * </p>
 */
public class KKLayoutFactory implements ILayoutFactory{

    @Override
    public Layout<INode, IConnection> transform(Graph<INode, IConnection> networkGraph) {

        return new KKLayout<>(networkGraph);
    }
}
