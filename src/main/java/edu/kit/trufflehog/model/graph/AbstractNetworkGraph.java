package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 * <p>
 *     Interface for the network graph.
 * </p>
 */
public abstract class AbstractNetworkGraph extends DirectedSparseMultigraph<INode, IConnection> {

    /**
     * <p>
     *     Adds a new {@link NetworkNode} to the graph to represent a new device.
     * </p>
     *
     * @param node NetworkNode to add
     */
    public abstract void addNetworkNode(NetworkNode node);

    /**
     * <p>
     *     Adds a new {@link NetworkEdge} to the graph to represent a new communication protocol used by two nodes.
     * </p>
     *
     * @param from NetworkNode sending packages
     * @param to NetworkNode receiving packages
     * @return The edge that was added.
     */
    public abstract NetworkEdge addNetworkEdge(NetworkNode from, NetworkNode to);


    public abstract NetworkNode getNetworkNodeByMACAddress(String macAddress);

    public abstract NetworkNode getNetworkNodeByIPAddress(String ipAddress);

    public abstract NetworkNode getNetworkNodeByDeviceName(String deviceName);

    public abstract NetworkEdge getNetworkEdge(NetworkNode a, NetworkNode b);

}