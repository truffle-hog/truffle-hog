package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * Created by jan on 23.02.16.
 */
public class PacketDataLoggingComponent implements IComponent {

    private final ObservableList<IPacketData> dataList;

    private ListProperty dataProperty;

    public PacketDataLoggingComponent() {

        dataList = FXCollections.observableArrayList();
        dataProperty = new SimpleListProperty<>(dataList);
    }

    public PacketDataLoggingComponent(Collection<IPacketData> data) {

        dataList = FXCollections.observableArrayList(data);
        dataProperty = new SimpleListProperty<>(dataList);
    }

    public ObservableList<IPacketData> getObservablePackets() {
        return dataList;
    }

    public ListProperty getObservablePacketsProperty() { return dataProperty; }

    public void addPacket(IPacketData packet) {
        dataList.add(packet);
    }

    @Override
    public String name() {
        return "Packet Logs";
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public IComponent createDeepCopy() {

        // because IPacketData is immutable, do not have to copy them explicitly
        return new PacketDataLoggingComponent(dataList);
    }

    @Override
    public boolean update(IComponent update) {
        return false;
    }
}
