package edu.kit.trufflehog.command.trufflecommand;


import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkIOPort;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.node.MulticastNodeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Valentin Kiechle on 12.02.2016.
 */
public class AddPacketDataCommandTest {

    private INetworkWritingPort writingPort;
    private List<IFilter> filterList;
    private IPacketData data;

    @Before
    public void setup() {
        writingPort = mock(NetworkIOPort.class);
        filterList = new LinkedList<IFilter>();
        data = mock(Truffle.class);
        when(data.getAttribute(Long.class, "sourceMacAddress")).thenReturn(1L);
        when(data.getAttribute(Long.class, "destMacAddress")).thenReturn(2L);
    }
    @After
    public void teardown() {
        writingPort = null;
        filterList= null;
        data = null;
    }

    @Test
    public void AddPacketDataCommandTest() {
        AddPacketDataCommand apdc = new AddPacketDataCommand(writingPort, data, filterList);
        apdc.execute();
        verify(writingPort, times(2)).writeNode(any(INode.class));
        verify(writingPort, times(1)).writeConnection(any(IConnection.class));
       // verify(any(INode.class), times(2)).getComposition().addComponent(any(MulticastNodeRendererComponent.class));
    }
}