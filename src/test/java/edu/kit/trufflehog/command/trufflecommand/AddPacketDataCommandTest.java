package edu.kit.trufflehog.command.trufflecommand;


import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.NetworkIOPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Filter;

import static org.mockito.Mockito.*;

/**
 * Created by Valentin Kiechle on 12.02.2016.
 */
public class AddPacketDataCommandTest {

    private INetworkWritingPort writingPort;
    private IFilter filter;
    private IPacketData data;

    @Before
    public void setup() {
        writingPort = mock(NetworkIOPort.class);
        filter = mock(IFilter.class);
        data = mock(Truffle.class);
        when(data.getAttribute(MacAddress.class, "sourceMacAddress")).thenReturn(new MacAddress(1L, false));
        when(data.getAttribute(MacAddress.class, "destMacAddress")).thenReturn(new MacAddress(2L, false));
    }
    @After
    public void teardown() {
        writingPort = null;
        filter = null;
        data = null;
    }

    @Test
    public void AddPacketDataCommandTest() {
        AddPacketDataCommand apdc = new AddPacketDataCommand(writingPort, data, filter);
        apdc.execute();
        verify(writingPort, times(2)).writeNode(any(INode.class));
        verify(writingPort, times(1)).writeConnection(any(IConnection.class));
       // verify(any(INode.class), times(2)).getComposition().addComponent(any(MulticastNodeRendererComponent.class));
    }
}