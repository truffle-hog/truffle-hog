package de.fraunhofer.iosb.trufflehog.model.graph;

/**<p>
 *Switch to jump between replay and live modes of the graph.
 * </p>
 */
public class NetworkGraphSwitch extends INetworkGraph {

	private GraphProxy liveGraph;
    private GraphProxy playbackGraph;
    private int state;

    /**<p>
     * Sets the viewing mode to playback
     * </p>
     */
	public void viewPlayback() {

	}

    /**<p>
     * Sets the viewing mode to live
     * </p>
     */
	public void viewLive() {

	}


	/**{@inheritDoc}
	 */
	public void addNetworkNode(NetworkNode node) {

	}


	/**{@inheritDoc}
	 */
	public void addNetworkEdge(NetworkNode from, NetworkNode to) {

	}

}
