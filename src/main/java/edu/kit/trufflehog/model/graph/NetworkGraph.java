package edu.kit.trufflehog.model.graph;

import java.io.Serializable;
import java.util.HashMap;

/**<p>
 * Stores {@link NetworkNode}s and {@link IConnection}s.
 * </p>
 */
public class NetworkGraph extends INetworkGraph implements Serializable {

	private HashMap networkNodes;
	private NetworkNode networkNode;
	private NetworkEdge networkEdge;

    /**{@inheritDoc}
     *
     */
	public void addNetworkEdge(NetworkNode from, NetworkNode to) {

	}

    /**{@inheritDoc}
     *
     */
	public void addNetworkNode(NetworkNode node) {

	}

}
