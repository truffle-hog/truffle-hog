package edu.kit.ipd.trufflehog.model.graph;

import jung.algorithms.layout.Layout;

/**<p>
 * Uses the Fruchterman-Reingold-algorithm from the jung library to present the {@link INetworkGraph}.
 * </p>
 */
public class FruchtermanReingoldLayout extends Layout<INode, IConnection> implements INetworkGraphLayout {

	/**<p>
     * Creates a new layout to present a given INetworkGraph.
     * </p>
	 * @param graph {@link INetworkGraph} to be drawn.
	 */
	public void FruchtermanReingoldLayout(INetworkGraph graph) {

	}

}
