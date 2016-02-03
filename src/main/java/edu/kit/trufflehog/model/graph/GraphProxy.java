package edu.kit.trufflehog.model.graph;

/**
 * <p>
 *     Graph deputy used to encapsulate the graph and thus make it interchangeable without having to update graph
 *     references of other objects.
 * </p>
 */
public class GraphProxy extends AbstractNetworkGraph {

	private AbstractNetworkGraph networkGraph;

    /**
     * <p>
     *     Sets the new reference graph.
     * </p>
     *
     * @param graph INetworkGraph to refer to
     */
	public void setGraph(AbstractNetworkGraph graph) {

	}

	@Override
	public void addNetworkNode(NetworkNode node) {

	}

	@Override
	public NetworkEdge addNetworkEdge(NetworkNode from, NetworkNode to) {
		return null;
	}

	@Override
	public NetworkNode getNetworkNodeByMACAddress(String macAddress) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public NetworkNode getNetworkNodeByIPAddress(String ipAddress) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public NetworkNode getNetworkNodeByDeviceName(String deviceName) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public NetworkEdge getNetworkEdge(NetworkNode a, NetworkNode b) {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

}
