package edu.kit.trufflehog.model.graph;

/**
 * <p>
 *     Switch to jump between replay and live modes of the graph.
 * </p>
 */
public class NetworkGraphSwitch extends AbstractNetworkGraph {

	private GraphProxy liveGraph;
    private GraphProxy playbackGraph;
    private int state;

    /**
	 * <p>
     *     Sets the viewing mode to playback
     * </p>
     */
	public void viewPlayback() {

	}

    /**
     * <p>
     *     Sets the viewing mode to live
     * </p>
     */
	public void viewLive() {

	}

    @Override
    public void addNetworkNode(NetworkNode node) {
        throw new UnsupportedOperationException("Unsupported Operation!");
    }

    @Override
    public NetworkEdge addNetworkEdge(NetworkNode from, NetworkNode to) {
        throw new UnsupportedOperationException("Unsupported Operation!");
    }

    @Override
	public NetworkNode getNetworkNodeByMACAddress(String macAddress) {
        throw new UnsupportedOperationException("Unsupported Operation!");
	}

	@Override
	public NetworkNode getNetworkNodeByIPAddress(String ipAddress) {
        throw new UnsupportedOperationException("Unsupported Operation!");
	}

	@Override
	public NetworkNode getNetworkNodeByDeviceName(String deviceName) {
        throw new UnsupportedOperationException("Unsupported Operation!");
	}

	@Override
	public NetworkEdge getNetworkEdge(NetworkNode a, NetworkNode b) {
        throw new UnsupportedOperationException("Unsupported Operation!");
	}

}
