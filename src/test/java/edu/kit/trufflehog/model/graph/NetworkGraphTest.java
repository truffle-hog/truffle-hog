package edu.kit.trufflehog.model.graph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hoehler on 13.02.2016.
 */
public class NetworkGraphTest {
    private INetworkGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = new NetworkGraph();
    }

    @Test(expected = NullPointerException.class)
    public void addNetworkEdgeTest() throws Exception {
        graph.addNetworkEdge(null);
    }

    @Test
    public void getNetworkEdgeTest() throws Exception {
        NetworkNode n1 = new NetworkNode();
        NetworkNode n2 = new NetworkNode();

        graph.addNetworkEdge(n1, n2);

        IConnection edge = graph.getNetworkEdge(n1, n2);
        assert (edge != null);
    }

    @Test
    public void getNodeByIpAddressTest() {
        INode node = new NetworkNode();
        node.setIpAddress(256);
        graph.addNetworkNode(node);

        INode test = graph.getNetworkNodeByIPAddress(256);
        assert (test.getIpAddress() == 256);
    }

    @Test
    public void getEdgeTest() {
        NetworkNode n1 = new NetworkNode();
        NetworkNode n2 = new NetworkNode();
        IConnection edge = new NetworkEdge(n1, n2);
        graph.addNetworkEdge(edge);

        IConnection test1 = graph.getNetworkEdge(n1, n2);
        IConnection test2 = graph.getNetworkEdge(n2, n1);

        assert (test1 != null && test2 == null);
    }

    @After
    public void tearDown() throws Exception {

    }
}