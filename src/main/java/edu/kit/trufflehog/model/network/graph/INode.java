package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

/**
 * <p>
 *     Interface used to represent nodes in the graph.
 * </p>
 */
public interface INode {

    void setIpAdress(String ipAddress);

    void setMacAdress(Long macAddress);

    void setDeviceName(String deviceName);

    void setTimeAdded(long timeAdded);

    void setLastUpdateTime(long lastUpdateTime);

    void setPackageCountIn(int packageCountIn);

    void setPackageCountOut(int packageCountOut);

    String getIpAdress();

    Long getMacAdress();

    String getDeviceName();

    long getTimeAdded();

    long getLastUpdateTime();

    int getPackageCountIn();

    int getPackageCountOut();

    void log(Truffle truffle);

}
