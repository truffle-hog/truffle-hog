package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.AbstractComponent;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.util.bindings.MyBindings;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * <p>
 *     This class holds all static node information like addresses and the device name.
 * </p>
 * @author Mark Giraud
 * @version 0.1
 */
public class NodeInfoComponent extends AbstractComponent implements IComponent {

    private final StringProperty deviceNameProperty = new SimpleStringProperty();
    private final ObjectProperty<IPAddress> ipAddressProperty = new SimpleObjectProperty<>();
    private final ReadOnlyObjectWrapper<MacAddress> macAddressProperty;

    private IComposition parent;

    public NodeInfoComponent(MacAddress macAddress) {
        this.macAddressProperty = new ReadOnlyObjectWrapper<>(macAddress);
    }

    /**
     * <p>
     *     Gets the device name.
     * </p>
     * @return the device name
     */
    public String getDeviceName() {
        return deviceNameProperty.getValue();
    }

    /**
     * <p>
     *     Sets the device name.
     * </p>
     * @param deviceName the name of the device. Must not be null.
     */
    public void setDeviceName(String deviceName) {
        if (deviceName == null)
            throw new NullPointerException("deviceName must not be null");

        deviceNameProperty.setValue(deviceName);

    }

    /**
     * <p>
     *     Gets the device name property.
     * </p>
     * @return the device name property
     */
    public StringProperty getDeviceNameProperty() {
        return deviceNameProperty;
    }

    /**
     * <p>
     *     Gets the ip address.
     * </p>
     * @return the ip address.
     */
    public IPAddress getIPAddress() {
        return ipAddressProperty.getValue();
    }

    /**
     * <p>
     *     Sets the ip address.
     * </p>
     * @param ip the ip address to set. Must not be null.
     */
    public void setIPAddress(IPAddress ip) {
        ipAddressProperty.setValue(ip);
    }

    /**
     * <p>
     *     Gets the ip address property.
     * </p>
     * @return the ip address property.
     */
    public ObjectProperty<IPAddress> getIpAddressProperty() {
        return ipAddressProperty;
    }

    /**
     * <p>
     *     Gets the mac address.
     * </p>
     * @return the mac address.
     */
    public MacAddress getMacAddress() {
        return macAddressProperty.getValue();
    }

    /**
     * <p>
     *     Gets the mac address property. This property is read only.
     * </p>
     * @return the mac address property.
     */
    public ReadOnlyObjectProperty<MacAddress> getMacAddressProperty() {
        return macAddressProperty.getReadOnlyProperty();
    }

    @Override
    public String name() {
        //TODO put this in property file
        return "Device info";
    }

    @Override
    public <T> T accept(IComponentVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NodeInfoComponent;
    }

    public StringBinding toStringBinding() {
        return MyBindings.nodeInfoStringBinding(this);
    }

    @Override
    public String toString() {

        if (deviceNameProperty.getValue() != null) {
            return deviceNameProperty.getValue() + " (" + macAddressProperty.getValue() + ")";
        }

        if (ipAddressProperty.getValue() != null) {
            return ipAddressProperty.getValue() + " (" + macAddressProperty.getValue() + ")";
        }

        return macAddressProperty.getValue().toString();
    }
}
