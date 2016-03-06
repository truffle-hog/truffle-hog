package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.IConnection;
import edu.kit.trufflehog.model.graph.NetworkNode;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
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

    private AddPacketDataCommand apdc;
    private INetworkGraph graph;
    private Filter filter;
    private List<Filter> filterList;
    private IPacketData truffle;

    @Before
    public void setup() {
        truffle = mock(IPacketData.class);
        when(truffle.getAttribute(Long.class, "sourceMacAddress")).thenReturn(1L);
        when(truffle.getAttribute(Long.class, "destinationMacAddress")).thenReturn(2L);

        filter = new Filter();
        filterList = new LinkedList<>();
        filterList.add(filter);

        graph = mock(INetworkGraph.class);

        apdc = new AddPacketDataCommand(graph, truffle, filterList);

    }

    @After
    public void teardown() {
        apdc = null;
        graph = null;
        filter = null;
        filterList = null;
        truffle = null;
    }

    @Test
    public void addTwoNewNodesByMacAddressToGraphTest() {
        when(graph.getNetworkNodeByMACAddress(1L)).thenReturn(null);
        when(graph.getNetworkNodeByMACAddress(2L)).thenReturn(null);
        apdc.execute();
        verify(graph, times(1)).addNetworkEdge(any(IConnection.class), any(NetworkNode.class), any(NetworkNode.class));
    }

    @Test
    public void addDestinationNodeByMacAddressToGraphTest() {
        NetworkNode testNode = mock(NetworkNode.class);
        when(graph.getNetworkNodeByMACAddress(1L)).thenReturn(testNode);
        apdc.execute();
        verify(graph, times(1)).addNetworkEdge(any(IConnection.class), eq(testNode), any(NetworkNode.class));
    }

    @Test
    public void addSourceNodeByMacAddressToGraphTest() {
        NetworkNode testNode = mock(NetworkNode.class);
        when(graph.getNetworkNodeByMACAddress(2L)).thenReturn(testNode);
        apdc.execute();
        verify(graph, times(1)).addNetworkEdge(any(IConnection.class), any(NetworkNode.class), eq(testNode));
    }

    @Test
    public void addNoNewNodesByMacAddressToGraphTest() {
        NetworkNode testNode = mock(NetworkNode.class);
        NetworkNode testNode2 = mock(NetworkNode.class);
        IConnection testEdge = mock(IConnection.class);
        when(graph.getNetworkNodeByMACAddress(1L)).thenReturn(testNode);
        when(graph.getNetworkNodeByMACAddress(2L)).thenReturn(testNode2);
        when(graph.getNetworkEdge(testNode,testNode2)).thenReturn(testEdge);
        apdc.execute();
        verify(graph, times(0)).addNetworkEdge(testEdge, testNode, testNode2);
    }
}