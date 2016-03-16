package edu.kit.trufflehog.model.network.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

import java.awt.*;

/**
 * <p>
 *     Creates a {@link Layout} implementation of the Fruchterman-Reingold algorithm.
 * </p>
 */
public class FRLayout2Factory implements Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> {

    @Override
    public Layout<INode, IConnection> transform(Graph<INode, IConnection> networkGraph) {

        // TODO change to concurrent FRLAyout2
        return new FRLayout2<>(networkGraph);
    }
}
