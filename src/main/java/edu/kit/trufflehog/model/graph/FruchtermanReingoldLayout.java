package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout2;

/**<p>
 * Uses the Fruchterman-Reingold-algorithm from the jung library to present the {@link INetworkGraph}.
 * </p>
 */
public class FruchtermanReingoldLayout extends FRLayout2 implements INetworkGraphLayout {

	/**<p>
     * Creates a new layout to present a given INetworkGraph.
     * </p>
	 * @param graph {@link INetworkGraph} to be drawn.
	 */
	FruchtermanReingoldLayout(INetworkGraph graph) {
		super(graph);
	}
}
