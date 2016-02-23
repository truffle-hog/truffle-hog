package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * Created by jan on 23.02.16.
 */
public class PacketDataLoggingComponent implements IComponent {

    private final ObservableList<IPacketData> dataList;

    public PacketDataLoggingComponent() {

        dataList = FXCollections.observableArrayList();
    }

    public PacketDataLoggingComponent(Collection<IPacketData> data) {

        dataList = FXCollections.observableArrayList(data);
    }

    public ObservableList<IPacketData> getObservablePackets() {
        return dataList;
    }

    public void addPacket(IPacketData packet) {
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

        return new PacketDataLoggingComponent(dataList);
    }
}
