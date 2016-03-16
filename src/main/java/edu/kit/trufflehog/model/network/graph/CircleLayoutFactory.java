package edu.kit.trufflehog.model.network.graph;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;

/**
 * Created by Infinity on 16.03.2016.
 */
public class CircleLayoutFactory implements org.apache.commons.collections15.Transformer<edu.uci.ics.jung.graph.Graph<INode, IConnection>, edu.uci.ics.jung.algorithms.layout.Layout<INode, IConnection>> {
    @Override
    public Layout<INode, IConnection> transform(Graph<INode, IConnection> iNodeIConnectionGraph) {
        return new CircleLayout<>(iNodeIConnectionGraph);
    }
}
