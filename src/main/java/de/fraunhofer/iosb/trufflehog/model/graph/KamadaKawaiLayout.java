package de.fraunhofer.iosb.trufflehog.model.graph;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;

/**<p>
 * Uses the Kamada-Kawai-algorithm from the jung library to present the {@link INetworkGraph}.
 * </p>
 */
public class KamadaKawaiLayout extends KKLayout<INode, IConnection> implements INetworkGraphLayout<INode, IConnection> {

    public KamadaKawaiLayout(Graph<INode, IConnection> g) {
        super(g);
    }

    /**<p>
     * Creates a new layout to present a given INetworkGraph.
     * </p>
     * @param graph {@link INetworkGraph} to be drawn.
     */
    public void KamadaKawaiLayout(INetworkGraph graph) {

    }

}
