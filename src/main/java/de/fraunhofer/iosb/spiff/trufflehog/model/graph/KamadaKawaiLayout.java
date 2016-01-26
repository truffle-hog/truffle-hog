package edu.kit.ipd.trufflehog.model.graph;

import jung.algorithms.layout.Layout;
import jung.algorithms.layout.KKLayout;

/**<p>
 * Uses the Kamada-Kawai-algorithm from the jung library to present the {@link INetworkGraph}.
 * </p>
 */
public class KamadaKawaiLayout extends KKLayout<INode, IConnection> implements INetworkGraphLayout {

    /**<p>
     * Creates a new layout to present a given INetworkGraph.
     * </p>
     * @param graph {@link INetworkGraph} to be drawn.
     */
    public void KamadaKawaiLayout(INetworkGraph graph) {

    }

}
