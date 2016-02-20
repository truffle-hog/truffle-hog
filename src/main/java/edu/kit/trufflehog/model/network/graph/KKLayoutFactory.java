package edu.kit.trufflehog.model.network.graph;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

/**
 * <p>
 *     Creates a {@link Layout} implementation of the Kamada-Kawai algorithm.
 * </p>
 */
public class KKLayoutFactory implements Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> {

    @Override
    public Layout<INode, IConnection> transform(Graph<INode, IConnection> networkGraph) {

        // TODO change to concurrent KK LAYOut
        return new KKLayout<>(networkGraph);
    }
}
