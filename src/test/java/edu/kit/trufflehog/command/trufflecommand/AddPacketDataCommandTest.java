package edu.kit.trufflehog.command.trufflecommand;


import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.*;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Valentin Kiechle on 12.02.2016.
 */
public class AddPacketDataCommandTest {

    private INetworkWritingPort writingPort;
    private IFilter filter;
    private IPacketData data;

    @Before
    public void setup() throws Exception {
        writingPort = mock(NetworkIOPort.class);
        filter = mock(IFilter.class);
        data = mock(Truffle.class);
        when(data.toString()).thenReturn("dataString");
        when(data.getAttribute(MacAddress.class, "sourceMacAddress")).thenReturn(new MacAddress(1L));
        when(data.getAttribute(MacAddress.class, "destMacAddress")).thenReturn(new MacAddress(2L));
        when(data.getAttribute(String.class, "deviceName")).thenReturn("device1");
        when(data.getAttribute(IPAddress.class, "sourceIPAddress")).thenReturn(new IPAddress(42L));
        when(data.getAttribute(Boolean.class, "isResponse")).thenReturn(true);
    }
    @After
    public void teardown() {
        writingPort = null;
        filter = null;
        data = null;
    }

    @Test
    public void addOnePacketTest() {
        AddPacketDataCommand apdc = new AddPacketDataCommand(writingPort, data, filter);
        apdc.execute();
        verify(writingPort, times(2)).writeNode(any(INode.class));
        verify(writingPort, times(1)).writeConnection(any(IConnection.class));
        // verify(any(INode.class), times(2)).getComposition().addComponent(any(MulticastNodeRendererComponent.class);
    }

    @Test
    public void toStringTest() {
        AddPacketDataCommand apdc = new AddPacketDataCommand(writingPort, data, filter);
        apdc.toString();
        assertEquals("dataString", apdc.toString());
    }
}