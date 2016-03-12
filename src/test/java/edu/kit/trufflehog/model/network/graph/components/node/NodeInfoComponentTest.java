package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.INode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * <p>
 *     This class contains all tests for the {@link NodeInfoComponent}
 * </p>
 * @author Mark Giraud
 */
public class NodeInfoComponentTest {

    private NodeInfoComponent component;
    private MacAddress mockedMAC;

    @Before
    public void setUp() throws Exception {
        mockedMAC = mock(MacAddress.class);
        component = new NodeInfoComponent(mockedMAC);
    }

    @After
    public void tearDown() throws Exception {

    }
}