package edu.kit.trufflehog.model.graph;
import org.apache.commons.collections15.keyvalue.MultiKey;
import org.apache.commons.collections15.map.MultiKeyMap;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 *     Stores {@link NetworkNode}s and {@link IConnection}s.
 * </p>
 */
public class NetworkGraph extends AbstractNetworkGraph implements Serializable {

    private HashMap<String, NetworkNode> networkNodes;
    private MultiKeyMap networkEdges;

    NetworkGraph() {
        networkNodes = new HashMap<>();
        networkEdges = new MultiKeyMap<>();
    }

    @Override
    public NetworkEdge addNetworkEdge(NetworkNode from, NetworkNode to) throws NullPointerException{
        if (from == null || to == null) throw new NullPointerException("Nodes do not exist!");
        NetworkEdge edge = new NetworkEdge(from, to);
        networkEdges.put(from, to, edge);

        return edge;
    }

    @Override
    public NetworkNode getNetworkNodeByMACAddress(String macAddress) {
        return networkNodes.get(macAddress);
    }

    @Override
    public NetworkNode getNetworkNodeByIPAddress(String ipAddress) {
        return networkNodes.get(ipAddress);
    }

    @Override
    public NetworkNode getNetworkNodeByDeviceName(String deviceName) {
        return networkNodes.get(deviceName);
    }

    @Override
    public NetworkEdge getNetworkEdge(NetworkNode a, NetworkNode b) {
        return (NetworkEdge)networkEdges.get(a, b);
    }

    @Override
    public void addNetworkNode(NetworkNode node) {
        if (node != null) {
            networkNodes.put(node.getDeviceName(), node);
            networkNodes.put(node.getIpAdress(), node);
            networkNodes.put(node.getMacAdress(), node);
        }
    }
}