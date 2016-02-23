package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by jan on 23.02.16.
 */
public class NetworkNodeTest {

    @Test
    public void testGetAddress() throws Exception {

    }

    @Test
    public void testCreateDeepCopy() throws Exception {

        final IAddress address = new MacAddress(0x000000ffffff);
        final INode node = new NetworkNode(address);

        node.addComponent(new NodeStatisticsComponent(1));

        final INode deepCopy = node.createDeepCopy();
        final NodeStatisticsComponent statCopy = deepCopy.getComponent(NodeStatisticsComponent.class);

        statCopy.setThroughputProperty(10);

        assertEquals(10, statCopy.getThroughput());
        assertEquals(1, node.getComponent(NodeStatisticsComponent.class).getThroughput());
    }

    @Test
    public void testAddComponent() throws Exception {
        final IAddress address = new MacAddress(0x000000ffffff);
        final INode node = new NetworkNode(address);

        final IComponent statistics = new NodeStatisticsComponent(1);

        node.addComponent(statistics);

        final NodeStatisticsComponent c = node.getComponent(NodeStatisticsComponent.class);

        assertEquals(1, c.getThroughput());

        c.incrementThroughput(5);

        assertEquals(6, c.getThroughput());
    }

    @Test
    public void testRemoveComponent() throws Exception {

        final IAddress address = new MacAddress(0x000000ffffff);
        final INode node = new NetworkNode(address);

        final IComponent statistics = new NodeStatisticsComponent(1);

        node.addComponent(statistics);

        node.removeComponent(NodeStatisticsComponent.class);
        assertNull(node.getComponent(NodeStatisticsComponent.class));

    }

    @Test
    public void testGetComponent() throws Exception {

        final IAddress address = new MacAddress(0x000000ffffff);
        final INode node = new NetworkNode(address);

        final IComponent statistics = new NodeStatisticsComponent(1);

        node.addComponent(statistics);

        assertEquals(statistics, node.getComponent(NodeStatisticsComponent.class));

    }

    @Test
    public void testCompareTo() throws Exception {

    }

    @Test
    public void testName() throws Exception {

    }

    @Test
    public void testIsMutable() throws Exception {

    }
}