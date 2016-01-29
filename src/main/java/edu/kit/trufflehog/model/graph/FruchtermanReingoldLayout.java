package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout2;

/**
 * <p>
 *     Uses the Fruchterman-Reingold-algorithm from the jung library to present the {@link AbstractNetworkGraph}.
 * </p>
 */
public class FruchtermanReingoldLayout extends FRLayout2<INode, IConnection> implements INetworkGraphLayout<INode, IConnection> {

	/**
     * <p>
     *     Creates a new layout to present a given INetworkGraph.
     * </p>
     *
	 * @param graph {@link AbstractNetworkGraph} to be drawn.
	 */
	FruchtermanReingoldLayout(AbstractNetworkGraph graph) {
		super(graph);
	}
}
