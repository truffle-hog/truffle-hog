package edu.kit.trufflehog.model.graph;
import org.apache.commons.collections15.keyvalue.MultiKey;
import org.apache.commons.collections15.map.MultiKeyMap;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 *     Stores {@link NetworkNode}s and {@link IConnection}s.
 * </p>
 */
public class NetworkGraph extends DirectedSparseGraph<INode, IConnection> implements INetworkGraph, Serializable {

    private MultiKeyMap networkNodes;
    private MultiKeyMap networkEdges;

    NetworkGraph() {
        networkNodes = new MultiKeyMap();
        networkEdges = new MultiKeyMap();
    }

    @Override
    public void addNetworkEdge(IConnection edge, INode src, INode dest) throws NullPointerException{
        if (src == null) throw new NullPointerException("Source node is null!");
        if (dest == null) throw new NullPointerException("Destination node from is null!");
        if (edge == null) throw new NullPointerException("Edge is null!");
        networkEdges.put(new MultiKey(src, dest), edge);
    }

    @Override
    public INode getNetworkNodeByMACAddress(long macAddress) {
        return (INode) networkNodes.get(macAddress);
    }

    @Override
    public INode getNetworkNodeByIPAddress(int ipAddress) {
        return (INode) networkNodes.get(ipAddress);
    }

    @Override
    public INode getNetworkNodeByDeviceName(String deviceName) {
        return (INode) networkNodes.get(deviceName);
    }

    @Override
    public IConnection getNetworkEdge(INode src, INode dest) {
        return (IConnection) networkEdges.get(src, dest);
    }

    @Override
    public void addNetworkNode(INode node) {
        if (node != null) {
            networkNodes.put(new MultiKey(node.getDeviceName()), node);
            networkNodes.put(new MultiKey(node.getIpAdress()), node);
            networkNodes.put(new MultiKey(node.getMacAdress()), node);
        }
    }
}