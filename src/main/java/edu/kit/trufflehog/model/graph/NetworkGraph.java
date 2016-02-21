package edu.kit.trufflehog.model.graph;
import org.apache.commons.collections15.keyvalue.MultiKey;
import org.apache.commons.collections15.map.MultiKeyMap;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.io.Serializable;

/**
 * <p>
 *     Stores {@link NetworkNode}s and {@link IConnection}s.
 * </p>
 */
public class NetworkGraph extends DirectedSparseGraph<INode, IConnection> implements INetworkGraph, Serializable {

    private MultiKeyMap<MultiKey, INode> networkNodes;
    private MultiKeyMap<MultiKey, IConnection> networkEdges;

    NetworkGraph() {
        networkNodes = new MultiKeyMap();
        networkEdges = new MultiKeyMap();
    }

    @Override
    public void addNetworkEdge(IConnection edge) throws NullPointerException {
        if (edge == null) throw new NullPointerException("Edge is null!");
        networkEdges.put(new MultiKey(edge.getSource(), edge.getDestination()), edge);
    }

    @Override
    public void addNetworkEdge(INode src, INode dest) throws NullPointerException {
        if (src == null) throw new NullPointerException("Source node is null!");
        if (dest == null) throw new NullPointerException("Destination node is null!");
        IConnection edge = new NetworkEdge(src, dest);
        networkEdges.put(new MultiKey(src, dest), edge);
    }

    @Override
    public INode getNetworkNodeByMACAddress(long macAddress) {
        return networkNodes.get(new MultiKey(macAddress));
    }

    @Override
    public INode getNetworkNodeByIPAddress(int ipAddress) {
        return networkNodes.get(new MultiKey(ipAddress));
    }

    @Override
    public INode getNetworkNodeByDeviceName(String deviceName) {
        return networkNodes.get(new MultiKey(deviceName));
    }

    @Override
    public IConnection getNetworkEdge(INode src, INode dest) {
        return networkEdges.get(new MultiKey(src, dest));
    }

    @Override
    public void addNetworkNode(INode node) {
        if (node != null) {
            networkNodes.put(new MultiKey(node.getDeviceName()), node);
            networkNodes.put(new MultiKey(node.getIpAddress()), node);
            networkNodes.put(new MultiKey(node.getMacAddress()), node);
        }
    }
}