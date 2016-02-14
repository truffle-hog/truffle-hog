package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.graph.DirectedGraph;

/**
 * Created by jan on 14.02.16.
 */
public interface INetworkGraph extends DirectedGraph<INode, IConnection> {

    INode getNetworkNodeByMACAddress(long macAddress);

    INode getNetworkNodeByIPAddress(int ipAddress);

    INode getNetworkNodeByDeviceName(String deviceName);

    INode getNetworkEdge(INode src, INode dest);

    void addNetworkEdge(IConnection connection, INode from, INode to);

    void addNetworkNode(INode node);
}
