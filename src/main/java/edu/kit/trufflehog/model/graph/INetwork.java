package edu.kit.trufflehog.model.graph;

/**
 * <p>
 *     Interface to access graph model data and perform changes on the graph.
 * </p>
 */
public interface INetwork {
    /**
     * <p>
     *     Adds a new INode to the graph if the node does not exist, updates the data if node does exist.
     * </p>
     * @param node INode to add or update
     */
    public void writeNode(INode node);

    /**
     * <p>
     *     Adds a new IConnection if the graph edge does not exist, updates the data if edge does exist.
     * </p>
     * @param connection IConnection to add or update
     */
    public void writeConnection(IConnection connection);

    /**
     * <p>
     *     Looks for an INode in the graph (with a certain IP address) and returns it if found.
     * </p>
     * @param ip IP-Address to identify the INode
     * @return INode with the provided ip if found, else null.
     */
    public INode getNodeByIPAddress(int ip);

    /**
     * <p>
     *     Looks for an INode in the graph (with a certain MAC address) and returns it if found.
     * </p>
     * @param mac MAC-Address to identify the INode
     * @return INode with the provided mac if found, else null.
     */
    public INode getNodeByMACAddress(long mac);

    /**
     * <p>
     *     Looks for an INode in the graph (with a certain device name) and returns it if found.
     * </p>
     * @param name Device name of the INode
     * @return INode with the provided name if found, else null.
     */
    public INode getNodeByDeviceName(String name);

    /**
     * <p>
     *     Looks for an IConnection (edge) between two INodes and returns it if found.
     * </p>
     * @param src Source node
     * @param dest Destination node
     * @return IConnection if edge is found, else null.
     */
    public IConnection getConnection(INode src, INode dest);
}
