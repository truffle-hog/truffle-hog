package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.util.IListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
    IListener<ITruffleCommand> mockedListener;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        mockedFilterList = new LinkedList<>();
        receiver = new UnixSocketReceiver(mock(INetworkWritingPort.class), mockedFilterList);

        mockedListener = (IListener<ITruffleCommand>) mock(IListener.class);
        receiver.addListener(mockedListener);

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

    // TODO remove ignore
    @Ignore
    @Test
    public void testConnect_ifSnortNotRunning() throws Exception {
        ArgumentCaptor<ITruffleCommand> argumentCaptor = ArgumentCaptor.forClass(ITruffleCommand.class);

        receiver.connect();

        verify(mockedListener).receive(argumentCaptor.capture());

    }

    @Test
    public void testConnect_ifSnortRunning() throws Exception {

        //TODO simulate a running snort by making a socket

    }

    @Test
    public void testDisconnect() throws Exception {

        //TODO simulate a running snort and verify if the connect and disconnect process works

        receiver.connect();

        receiver.disconnect();
    }
}