package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.INode;
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
public class PacketDataLoggingComponent implements IComponent {

    private final ObservableList<IPacketData> dataList;
    private ListProperty dataProperty;

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
    public boolean isMutable() {
        return true;
    }

    @Override
    public IComponent createDeepCopy() {

        // because IPacketData is immutable, do not have to copy them explicitly
        return new PacketDataLoggingComponent(dataList);
    }

    @Override
    public boolean update(INode update) {
        PacketDataLoggingComponent logComponent = update.getComposition().getComponent(PacketDataLoggingComponent.class);
        if (logComponent == null) return false;
        dataList.addAll(logComponent.getObservablePackets());

        return true;
    }
}
