package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Hoehler on 25.03.2016.
 * TODO: test javafx Platform stuff
 */
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
}