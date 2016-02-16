package edu.kit.trufflehog.model.graph;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

import java.io.Serializable;

/**
 * <p>
 *     Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode implements Serializable, INode {

	// TODO MAKE ALLE FINALL!!!!
	private Long macAdress;
	private String deviceName;
	private long timeAdded;
	private long lastUpdateTime;
	private int packageCountIn;
	private int packageCountOut;
	private String ipAdress;

	@Override
	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	@Override
	public void setMacAdress(Long macAdress) {
		this.macAdress = macAdress;
	}

	@Override
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public void setTimeAdded(long timeAdded) {
		this.timeAdded = timeAdded;
	}

	@Override
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public void setPackageCountIn(int packageCountIn) {
		this.packageCountIn = packageCountIn;
	}

	@Override
	public void setPackageCountOut(int packageCountOut) {
		this.packageCountOut = packageCountOut;
	}

	@Override
	public String getIpAdress() {
		return ipAdress;
	}

	@Override
	public Long getMacAdress() {
		return macAdress;
	}

	@Override
	public String getDeviceName() {
		return deviceName;
	}

	@Override
	public long getTimeAdded() {
		return timeAdded;
	}

	@Override
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	@Override
	public int getPackageCountIn() {
		return packageCountIn;
	}

	@Override
	public int getPackageCountOut() {
		return packageCountOut;
	}


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
	@Override
	public void log(Truffle truffle) {

	}

    /*add getter for properties!
     */

}
