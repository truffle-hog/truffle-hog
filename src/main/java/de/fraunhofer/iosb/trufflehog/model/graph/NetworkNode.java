package de.fraunhofer.iosb.trufflehog.model.graph;

import de.fraunhofer.iosb.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

import java.io.Serializable;

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
	//private TruffleLogger truffleLogger;

//    /**<p>
//     * Provides the internal logger to access logs and statistics.
//     * </p>
//     * @return {@link TruffleLogger} of this node
//     */
//	public TruffleLogger getLogger() {
//	}

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
