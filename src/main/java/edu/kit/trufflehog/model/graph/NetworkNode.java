package edu.kit.trufflehog.model.graph;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

import java.io.Serializable;

/**
 * <p>
 *     Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode implements Serializable, INode {

	private String ipAdress;

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public void setMacAdress(String macAdress) {
		this.macAdress = macAdress;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setTimeAdded(int timeAdded) {
		this.timeAdded = timeAdded;
	}

	public void setLastUpdateTime(int lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void setPackageCountIn(int packageCountIn) {
		this.packageCountIn = packageCountIn;
	}

	public void setPackageCountOut(int packageCountOut) {
		this.packageCountOut = packageCountOut;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public String getMacAdress() {
		return macAdress;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public int getTimeAdded() {
		return timeAdded;
	}

	public int getLastUpdateTime() {
		return lastUpdateTime;
	}

	public int getPackageCountIn() {
		return packageCountIn;
	}

	public int getPackageCountOut() {
		return packageCountOut;
	}

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

    /**
     * <p>
     *     Logs a Truffle package with the internal TruffleLogger.
     * </p>
     *
     * @param truffle {@link Truffle} to log
     */
	public void log(Truffle truffle) {

	}

    /*add getter for properties!
     */

}
