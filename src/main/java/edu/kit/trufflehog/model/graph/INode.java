package edu.kit.trufflehog.model.graph;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

/**
 * <p>
 *     Interface used to represent nodes in the graph.
 * </p>
 */
public interface INode {

    void setIpAddress(int ipAddress);

    void setMaxAddress(long macAddress);

    void setDeviceName(String deviceName);

    void setTimeAdded(int timeAdded);

    void setLastUpdateTime(int lastUpdateTime);

    void setPackageCountIn(int packageCountIn);

    void setPackageCountOut(int packageCountOut);

    int getIpAddress();

    long getMacAddress();

    String getDeviceName();

    int getTimeAdded();

    int getLastUpdateTime();

    int getPackageCountIn();

    int getPackageCountOut();

    void log(Truffle truffle);

}
