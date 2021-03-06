package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
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

/*        final ICopyCreator copyCreator = new TapeCopyCreator();

        final IAddress address = new MacAddress(0x000000ffffff);
        final INode node = new NetworkNode(address);

        node.addComponent(new NodeStatisticsComponent(1));

        node.getComponent(NodeStatisticsComponent.class).setCommunicationCountProperty(4);

        node.getComponent(NodeStatisticsComponent.class).incrementThroughput(1);

        final INode deepCopy = node.createDeepCopy(copyCreator);
        final NodeStatisticsComponent statCopy = deepCopy.getComponent(NodeStatisticsComponent.class);

        //statCopy.setCommunicationCountProperty(10);

        assertEquals(5, statCopy.getCommunicationCount());
        assertEquals(5, node.getComponent(NodeStatisticsComponent.class).getCommunicationCount());


        statCopy.setCommunicationCountProperty(10);

        assertEquals(10, statCopy.getCommunicationCount());
        assertEquals(5, node.getComponent(NodeStatisticsComponent.class).getCommunicationCount());*/


    }

    @Test
    public void testAddComponent() throws Exception {
        final IAddress address = new MacAddress(0x000000ffffff);
        final INode node = new NetworkNode(address);

        final IComponent statistics = new NodeStatisticsComponent(1,0);

        node.addComponent(statistics);

        final NodeStatisticsComponent c = node.getComponent(NodeStatisticsComponent.class);

        assertEquals(1, c.getCommunicationCount());
    }

    @Test
    public void testRemoveComponent() throws Exception {

        final IAddress address = new MacAddress(0x000000ffffff);
        final INode node = new NetworkNode(address);

        final IComponent statistics = new NodeStatisticsComponent(1,0);

        node.addComponent(statistics);

        node.removeComponent(NodeStatisticsComponent.class);
        assertNull(node.getComponent(NodeStatisticsComponent.class));

    }

    @Test
    public void testGetComponent() throws Exception {

        final IAddress address = new MacAddress(0x000000ffffff);
        final INode node = new NetworkNode(address);

        final IComponent statistics = new NodeStatisticsComponent(1,0);

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