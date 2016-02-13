package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.AbstractNetworkGraph;
import edu.kit.trufflehog.model.graph.NetworkNode;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.nio.ch.Net;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Valentin Kiechle on 12.02.2016.
 */
public class AddPacketDataCommandTest {

    private AddPacketDataCommand apdc;
    private AbstractNetworkGraph graph;
    private Filter filter;
    private List<Filter> filterList;
    private IPacketData truffle;

    @Before
    public void setup() {
        truffle = mock(IPacketData.class);
        when(truffle.getAttribute(String.class, "sourceMacAddress")).thenReturn("sourceTestAddress");
        when(truffle.getAttribute(String.class, "destinationMacAddress")).thenReturn("destinationTestAddress");

        filter = new Filter();
        filterList = new LinkedList<>();
        filterList.add(filter);

        graph = mock(AbstractNetworkGraph.class);

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
        when(graph.getNetworkNodeByMACAddress("sourceTestAddress")).thenReturn(null);
        when(graph.getNetworkNodeByMACAddress("destinationTestAddress")).thenReturn(null);
        apdc.execute();
        verify(graph, times(1)).addNetworkEdge(any(NetworkNode.class), any(NetworkNode.class));
    }

    @Test
    public void addDestinationNodeByMacAddressToGraphTest() {
        NetworkNode testNode = mock(NetworkNode.class);
        when(graph.getNetworkNodeByMACAddress("sourceTestAddress")).thenReturn(testNode);
        apdc.execute();
        verify(graph, times(1)).addNetworkEdge(eq(testNode), any(NetworkNode.class));
    }

    @Test
    public void addSourceNodeByMacAddressToGraphTest() {
        NetworkNode testNode = mock(NetworkNode.class);
        when(graph.getNetworkNodeByMACAddress("destinationTestAddress")).thenReturn(testNode);
        apdc.execute();
        verify(graph, times(1)).addNetworkEdge(any(NetworkNode.class), eq(testNode));
    }

    @Test
    public void addNoNewNodesByMacAddressToGraphTest() {
        NetworkNode testNode = mock(NetworkNode.class);
        NetworkNode testNode2 = mock(NetworkNode.class);
        when(graph.getNetworkNodeByMACAddress("sourceTestAddress")).thenReturn(testNode);
        when(graph.getNetworkNodeByMACAddress("destinationTestAddress")).thenReturn(testNode2);
        apdc.execute();
        verify(graph, times(1)).addNetworkEdge(testNode, testNode2);
    }
}