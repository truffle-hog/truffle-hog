package edu.kit.trufflehog.model.graph;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

/**
 * <p>
 *     Interface used to represent nodes in the graph.
 * </p>
 */
public interface INode {

    void setIpAdress(String ipAdress);

    void setMacAdress(String macAdress);

    void setDeviceName(String deviceName);

    void setTimeAdded(int timeAdded);

    void setLastUpdateTime(int lastUpdateTime);

    void setPackageCountIn(int packageCountIn);

    void setPackageCountOut(int packageCountOut);

    String getIpAdress();

    String getMacAdress();

    String getDeviceName();

    int getTimeAdded();

    int getLastUpdateTime();

    int getPackageCountIn();

    int getPackageCountOut();

    void log(Truffle truffle);

}
