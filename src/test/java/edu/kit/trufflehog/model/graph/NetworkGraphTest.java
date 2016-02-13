package edu.kit.trufflehog.model.graph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hoehler on 13.02.2016.
 */
public class NetworkGraphTest {
    private AbstractNetworkGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = new NetworkGraph();
    }

    @Test(expected = NullPointerException.class)
    public void addNetworkEdgeTest() throws Exception {
        NetworkEdge edge = graph.addNetworkEdge(new NetworkNode(), null);
    }

    @Test
    public void getNetworkEdgeTest() throws Exception {
        NetworkNode n1 = new NetworkNode();
        NetworkNode n2 = new NetworkNode();
        graph.addNetworkEdge(n1, n2);

        NetworkEdge edge = graph.getNetworkEdge(n1, n2);
        assert (edge != null);
    }

    @After
    public void tearDown() throws Exception {

    }
}