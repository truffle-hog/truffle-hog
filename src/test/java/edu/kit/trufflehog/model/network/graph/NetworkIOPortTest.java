package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jan on 23.02.16.
 */
public class NetworkIOPortTest {

    private INetworkIOPort port;

    @Before
    public void setUp() throws Exception {

        port = new NetworkIOPort(new ConcurrentDirectedSparseGraph<>());

    }

    @Test
    public void testWriteConnection() throws Exception {

    }

    @Test
    public void testWriteNode() throws Exception {

    }

    @Test
    public void testGetNetworkNodeByAddress() throws Exception {

        final INode networkNode = new NetworkNode(new MacAddress(0xffL));

        port.writeNode(networkNode);

        final INode getNode = port.getNetworkNodeByAddress(new MacAddress(0xffL));

        assertEquals(networkNode, getNode);
    }

    @Test
    public void testGetNetworkConnectionByAddress() throws Exception {

        final INode networkNodeSrc = new NetworkNode(new MacAddress(0xffL));
        final INode networkNodeDest = new NetworkNode(new MacAddress(0x11L));

        final IConnection connection = new NetworkConnection(networkNodeSrc, networkNodeDest);

        port.writeNode(networkNodeSrc);
        port.writeNode(networkNodeDest);
        port.writeConnection(connection);

        final IConnection getCon = port.getNetworkConnectionByAddress(new MacAddress(0xffL), new MacAddress(0x11L));

        assertEquals(connection, getCon);
    }
}