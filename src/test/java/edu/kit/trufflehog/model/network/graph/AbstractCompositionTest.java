package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.components.ComponentInfoCollector;
import edu.kit.trufflehog.model.network.graph.components.ComponentInfoVisitor;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * \brief
 * \details
 * \date 15.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class AbstractCompositionTest {

    private static final Logger logger = LogManager.getLogger(AbstractCompositionTest.class);

    private NetworkNode node;

    @Before
    public void setUp() throws Exception {

        final IAddress mac = new MacAddress(0x0);

        node = new NetworkNode(mac, new NodeStatisticsComponent(5,3), new NodeInfoComponent((MacAddress) mac));

    }

    @After
    public void tearDown() throws Exception {

        node = null;

    }

    @Test
    public void testAddComponent() throws Exception {

    }

    @Test
    public void testRemoveComponent() throws Exception {

    }

    @Test
    public void testGetComponent() throws Exception {

    }

    @Test
    public void testSize() throws Exception {

    }

    @Test
    public void testIsEmpty() throws Exception {

    }

    @Test
    public void testContains() throws Exception {

    }

    @Test
    public void testIterator() throws Exception {

        ComponentInfoVisitor vis = new ComponentInfoVisitor();

        ComponentInfoCollector col = new ComponentInfoCollector(vis);

        logger.debug(node.parallelStream().collect(col));

        //node.stream().forEach(logger::debug);

    }

    @Test
    public void testToArray() throws Exception {

    }

    @Test
    public void testToArray1() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testRemove() throws Exception {

    }

    @Test
    public void testContainsAll() throws Exception {

    }

    @Test
    public void testAddAll() throws Exception {

    }

    @Test
    public void testRemoveAll() throws Exception {

    }

    @Test
    public void testRetainAll() throws Exception {

    }

    @Test
    public void testClear() throws Exception {

    }
}