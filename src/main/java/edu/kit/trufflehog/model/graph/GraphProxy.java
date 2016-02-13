package edu.kit.trufflehog.model.graph;

/**
 * <p>
 *     Graph deputy used to encapsulate the graph and thus make it interchangeable without having to update graph
 *     references of other objects.
 * </p>
 */
public class GraphProxy extends AbstractNetworkGraph {

	private AbstractNetworkGraph networkGraph;

    GraphProxy(AbstractNetworkGraph graph) throws NullPointerException{
        if (graph == null) {
            throw new NullPointerException("Graph is not existing!");
        }
        networkGraph = graph;
    }

    /**
     * <p>
     *     Sets the new reference graph.
     * </p>
     *
     * @param graph INetworkGraph to refer to
     */
	public void setGraph(AbstractNetworkGraph graph) {
        if (graph != null) {
            networkGraph = graph;
        }
	}

	@Override
	public void addNetworkNode(NetworkNode node) {
        networkGraph.addNetworkNode(node);
	}

	@Override
	public NetworkEdge addNetworkEdge(NetworkNode from, NetworkNode to) {
        return networkGraph.addNetworkEdge(from, to);
	}

	@Override
	public NetworkNode getNetworkNodeByMACAddress(String macAddress) {
        return networkGraph.getNetworkNodeByMACAddress(macAddress);
	}

	@Override
	public NetworkNode getNetworkNodeByIPAddress(String ipAddress) {
        return networkGraph.getNetworkNodeByIPAddress(ipAddress);
	}

	@Override
	public NetworkNode getNetworkNodeByDeviceName(String deviceName) {
        return networkGraph.getNetworkNodeByDeviceName(deviceName);
	}

	@Override
	public NetworkEdge getNetworkEdge(NetworkNode a, NetworkNode b) {
        return networkGraph.getNetworkEdge(a, b);
	}

}
