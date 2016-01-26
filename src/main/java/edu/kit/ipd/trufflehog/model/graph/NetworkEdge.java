package edu.kit.ipd.trufflehog.model.graph;

import java.io.Serializable;

/**<p>
 * Edge in the graph to represent a relation between two devices. Stores important statistics about the ongoing communication.
 * </p>
 */
public class NetworkEdge implements IConnection, Serializable {

	private long totalPacketCount;
	private int connectionType;
	private long active;

    /*properties getter!
     */
}
