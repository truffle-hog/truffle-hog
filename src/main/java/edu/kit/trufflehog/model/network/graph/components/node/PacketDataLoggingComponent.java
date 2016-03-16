package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.AbstractComponent;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * <p>
 *     Component to store outgoing and incoming IPacketData of nodes. Provides JavaFX property to access/observe
 *     the data.
 * </p>
 */
public class PacketDataLoggingComponent extends AbstractComponent implements IComponent {

    private final ObservableList<IPacketData> dataList;
    private final ListProperty dataProperty;

    /**
     * <p>
     *     Creates and initializes the component.
     * </p>
     */
    public PacketDataLoggingComponent() {



        dataList = FXCollections.observableArrayList();
        dataProperty = new SimpleListProperty<>(dataList);
    }

    /**
     * <p>
     *     Creates and initializes the component using the provided Collection of IPacketData.
     * </p>
     */
    public PacketDataLoggingComponent(Collection<IPacketData> data) {

        dataList = FXCollections.observableArrayList(data);
        dataProperty = new SimpleListProperty<>(dataList);
    }

    public ObservableList<IPacketData> getObservablePackets() {
        return dataList;
    }

    public ListProperty getObservablePacketsProperty() { return dataProperty; }

    /**
     * <p>
     *     Adds the provided IPacketData to the list.
     * </p>
     * @param packet The packet to add to the list (must not be null).
     */
    public void addPacket(IPacketData packet) {
        if (packet == null) throw new NullPointerException("packet must not be null!");
        dataList.add(packet);
    }

    @Override
    public String name() {
        return "Packet Logs";
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
    public boolean equals(Object o) {
        return (o instanceof PacketDataLoggingComponent);
    }
}