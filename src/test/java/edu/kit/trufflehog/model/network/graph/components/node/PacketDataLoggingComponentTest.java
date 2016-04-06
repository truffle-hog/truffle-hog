package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Hoehler on 25.03.2016.
 */
public class PacketDataLoggingComponentTest {
    @Test
    public void addPacketData() throws Exception {
        PacketDataLoggingComponent component = new PacketDataLoggingComponent();
        IPacketData packet = Mockito.mock(Truffle.class);
        ObservableList<IPacketData> list = FXCollections.observableArrayList();

        when(packet.getAttribute(MacAddress.class, "sourceMacAddress")).thenReturn(new MacAddress(123));

        assertEquals(0, component.getObservablePackets().size());

        component.addPacket(packet);

        assertEquals(1, component.getObservablePackets().size());

        IPacketData packetTest = component.getObservablePackets().get(0);
        assertTrue(packetTest.getAttribute(MacAddress.class, "sourceMacAddress").toString().equals((new MacAddress(123).toString())));
    }
}