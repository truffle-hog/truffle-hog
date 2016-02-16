package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.graph.DirectedGraph;

/**
 * The Interface for Network Graphs used by TruffleHog
 */
public interface INetworkGraph extends DirectedGraph<INode, IConnection> {

    INode getNetworkNodeByMACAddress(long macAddress);

    INode getNetworkNodeByIPAddress(int ipAddress);

    INode getNetworkNodeByDeviceName(String deviceName);

    IConnection getNetworkEdge(INode src, INode dest);

    void addNetworkEdge(IConnection connection, INode from, INode to);

    void addNetworkNode(INode node);
}
