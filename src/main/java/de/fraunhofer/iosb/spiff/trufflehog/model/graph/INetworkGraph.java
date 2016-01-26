package de.fraunhofer.iosb.spiff.trufflehog.model.graph;

/**<p>
 * Interface for the network graph.
 * </p>
 */
public interface INetworkGraph {

    /**<p>
     *Adds a new {@link NetworkNode} to the graph to represent a new device.
     * </p>
     * @param node NetworkNode to add
     */
	public void addNetworkNode(NetworkNode node);

    /**<p>
     *Adds a new {@link NetworkEdge} to the graph to represent a new communication protocol used by two nodes.
     * </p>
     * @param from NetworkNode sending packages
     * @param to NetworkNode receiving packages
     */
	public void addNetworkEdge(NetworkNode from, NetworkNode to);

}
