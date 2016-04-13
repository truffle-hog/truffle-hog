package edu.kit.trufflehog.model.network.graph;

import de.saxsys.javafx.test.JfxRunner;
import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Hoehler on 25.03.2016.
 * TODO: test javafx Platform stuff
 */
@RunWith(JfxRunner.class)
public class LiveUpdaterTest {
    private LiveUpdater updater;

    @Before
    public void setUp() {
        updater = new LiveUpdater();
    }

    @Test
    public void update_BasicEdgeRenderer() throws Exception {
        BasicEdgeRenderer renderer1 = new BasicEdgeRenderer();
        BasicEdgeRenderer renderer2 = new BasicEdgeRenderer();
        renderer1.setCurrentBrightness(0.3);

        updater.update(renderer1, renderer2);

        assertEquals(1.0f, renderer1.getCurrentBrightness(), 0.0001);
    }

    @Test
    public void update_NodeInfoComponent() throws Exception {
        IPAddress address1 = new IPAddress(100);
        IPAddress address2 = new IPAddress(101);
        NodeInfoComponent component1 = new NodeInfoComponent(new MacAddress(100));
        NodeInfoComponent component2 = new NodeInfoComponent(new MacAddress(101));
        component1.setIPAddress(address1);
        component2.setDeviceName("new name");
        component2.setIPAddress(address2);

        updater.update(component1, component2);

        assertEquals("new name", component1.getDeviceName());
        assertEquals(address2.toString(), component1.getIPAddress().toString());
    }

    @Test
    @Ignore
    //TODO fix: sometimes test fails sometimes not (scheduling of runlater stuff?)
    public void update_NodeStatisticsComponent() throws Exception {
        NodeStatisticsComponent component1 = new NodeStatisticsComponent(0, 0);
        NodeStatisticsComponent component2 = new NodeStatisticsComponent(0, 0);
        component1.setIncomingCount(1);
        component1.setOutgoingCount(3);
        component2.setIncomingCount(10);
        component2.setOutgoingCount(13);

        assertTrue(updater.update(component1, component2));

        assertEquals(11, component1.getIncomingCount());
        assertEquals(16, component1.getOutgoingCount());
        assertEquals(10, component2.getIncomingCount());
        assertEquals(13, component2.getOutgoingCount());
    }
}