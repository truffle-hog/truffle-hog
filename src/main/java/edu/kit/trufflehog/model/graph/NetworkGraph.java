package edu.kit.trufflehog.model.graph;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 *     Stores {@link NetworkNode}s and {@link IConnection}s.
 * </p>
 */
public class NetworkGraph extends AbstractNetworkGraph implements Serializable {

    private HashMap networkNodes;
    private NetworkNode networkNode;
    private NetworkEdge networkEdge;

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

    @Override
    public void addNetworkNode(NetworkNode node) {

    }
}