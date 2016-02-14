package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 *     Stores {@link NetworkNode}s and {@link IConnection}s.
 * </p>
 */
public class NetworkGraph extends DirectedSparseGraph<INode, IConnection> implements INetworkGraph, Serializable {

    private HashMap networkNodes;


    @Override
    public INode getNetworkNodeByMACAddress(long macAddress) {
        return null;
    }

    @Override
    public INode getNetworkNodeByIPAddress(int ipAddress) {
        return null;
    }

    @Override
    public INode getNetworkNodeByDeviceName(String deviceName) {
        return null;
    }

    @Override
    public INode getNetworkEdge(INode src, INode dest) {
        return null;
    }

    @Override
    public void addNetworkEdge(IConnection connection, INode from, INode to) {

    }

    @Override
    public void addNetworkNode(INode node) {

    }
}