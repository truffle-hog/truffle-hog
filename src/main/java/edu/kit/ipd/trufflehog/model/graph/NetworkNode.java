package edu.kit.ipd.trufflehog.model.graph;

import java.io.Serializable;

import edu.kit.ipd.trufflehog.model.graphlog.TruffleLogger;
import edu.kit.ipd.trufflehog.service.truffleprocessor.Truffle;

/**<p>
 * Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode implements Serializable, INode {

	private String ipAdress;
	private String macAdress;
	private String deviceName;
	private int timeAdded;
	private int lastUpdateTime;
	private int packageCountIn;
	private int packageCountOut;
	private TruffleLogger truffleLogger;

    /**<p>
     * Provides the internal logger to access logs and statistics.
     * </p>
     * @return {@link TruffleLogger} of this node
     */
	public TruffleLogger getLogger() {
	}

    /**<p>
     *Logs a Truffle package with the internal TruffleLogger.
     *</p>
     * @param truffle {@link Truffle} to log
     */
	public void log(Truffle truffle) {

	}

    /*add getter for properties!
     */

}
