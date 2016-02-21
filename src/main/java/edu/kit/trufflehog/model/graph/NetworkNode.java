package edu.kit.trufflehog.model.graph;

import edu.kit.trufflehog.model.truffledatalog.TruffleLogger;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

import java.io.Serializable;

/**
 * <p>
 *     Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode implements Serializable, INode {

	private int ipAddress;
    private long macAddress;
    private String deviceName;
    private int timeAdded;
    private int lastUpdateTime;
    private int packageCountIn;
    private int packageCountOut;
    private TruffleLogger truffleLogger;

    NetworkNode() {
        truffleLogger = new TruffleLogger();
    }

	public void setIpAddress(int ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public void setMaxAddress(long macAddress) {
		this.macAddress = macAddress;
	}

	@Override
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public void setTimeAdded(int timeAdded) {
		this.timeAdded = timeAdded;
	}

	@Override
	public void setLastUpdateTime(int lastUpdateTime) {
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
	public int getIpAddress() {
		return ipAddress;
	}

	@Override
	public long getMacAddress() {
		return macAddress;
	}

	@Override
	public String getDeviceName() {
		return deviceName;
	}

	@Override
	public int getTimeAdded() {
		return timeAdded;
	}

	@Override
	public int getLastUpdateTime() {
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

    /**<p>
       * Provides the internal logger to access logs and statistics.
       * </p>
       * @return {@link TruffleLogger} of this node
     */
	public TruffleLogger getLogger() {
        return truffleLogger;
	}

    /**
     * <p>
     *     Logs a Truffle package with the internal TruffleLogger.
     * </p>
     *
     * @param truffle {@link Truffle} to log
     */
	@Override
	public void log(Truffle truffle) {
        truffleLogger.log(truffle);
	}
}
