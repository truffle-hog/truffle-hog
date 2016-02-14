package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;

/**
 * <p>
 *     Creates a {@link Layout} implementation of the Fruchterman-Reingold algorithm.
 * </p>
 */
public class FRLayout2Factory implements ILayoutFactory{

    @Override
    public Layout<INode, IConnection> transform(Graph<INode, IConnection> networkGraph) {

        return new FRLayout2<>(networkGraph);
    }
}
