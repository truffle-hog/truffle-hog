package edu.kit.trufflehog.model.graph;

import edu.kit.trufflehog.model.graph.jungconcurrent.ConcurrentFRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

/**
 * <p>
 *     Creates a {@link Layout} implementation of the Fruchterman-Reingold algorithm.
 * </p>
 */
public class FRLayoutFactory implements Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> {

    @Override
    public Layout<INode, IConnection> transform(Graph<INode, IConnection> networkGraph) {

        return new ConcurrentFRLayout<>(networkGraph);
    }
}
