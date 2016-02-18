package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * <p>
 *     This class contains all tests for {@link UnixSocketReceiver}
 * </p>
 *
 * @author Mark
 */
public class UnixSocketReceiverTest {

    UnixSocketReceiver receiver;
    List<Filter> mockedFilterList;
    Thread testRunner;

    @Before
    public void setUp() throws Exception {
        mockedFilterList = new LinkedList<>();
        receiver = new UnixSocketReceiver(mock(INetworkGraph.class), mockedFilterList);

        testRunner = new Thread(receiver);
        testRunner.start();
    }

    @After
    public void tearDown() throws Exception {
        testRunner.interrupt();

        mockedFilterList = null;
        receiver = null;
        testRunner = null;
    }

    @Test(expected = SnortPNPluginNotRunningException.class)
    public void testConnect_ifSnortNotRunning() throws Exception {
        try {
            receiver.connect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDisconnect() throws Exception {

    }
}