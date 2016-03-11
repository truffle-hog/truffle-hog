package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jan on 23.02.16.
 */
public class NodeStatisticsComponent implements IComponent {

    private static final Logger logger = LogManager.getLogger(NodeStatisticsComponent.class);

    private MacAddress macAddress = new MacAddress(0);
    private IPAddress ipAddress = new IPAddress(0);
    private final IntegerProperty throughputProperty = new SimpleIntegerProperty(1);
    private String deviceName = "";
    private boolean isLegal = true;

    public NodeStatisticsComponent(int initial) {
        throughputProperty.set(initial);
    }

    public NodeStatisticsComponent(MacAddress macAddress) {
        if (macAddress == null) throw new NullPointerException("macAddress must not be null!");
        this.macAddress = macAddress;
    }

    public IntegerProperty getThroughputProperty() {
        return throughputProperty;
    }

    public int getThroughput() {
        return throughputProperty.get();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public IPAddress getIpAddress() {
        return ipAddress;
    }

    public boolean isLegal() {
        return isLegal;
    }

    public void setLegal(boolean legal) {
        isLegal = legal;
    }

    public void setThroughputProperty(int value) {
        throughputProperty.set(value);
    }

    public void setDeviceName(String deviceName) {
        if (deviceName == null) throw new NullPointerException("deviceName must not be null!");
        this.deviceName = deviceName;
    }

    public void setMacAddress(MacAddress macAddress) {
        if (macAddress == null) throw new NullPointerException("macAddress must not be null!");
        this.macAddress = macAddress;
    }

    public void setIPAddress(IPAddress ipAddress) {
        if (ipAddress == null) throw new NullPointerException("ipAddress must not be null!");
        this.ipAddress = ipAddress;
    }

    public void incrementThroughput(int step) {
        setThroughputProperty(getThroughput() + step);
    }

    @Override
    public String name() {
        return "Traffic info";
    }

    @Override
    public boolean isMutable() {
        return true;
    }


    @Override
    public String toString() {

        return name() + ": " + "Throughput=" + getThroughput();

    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        if (copyCreator == null) throw new NullPointerException("copyCreator must not be null!");
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof NodeStatisticsComponent);
    }
}
