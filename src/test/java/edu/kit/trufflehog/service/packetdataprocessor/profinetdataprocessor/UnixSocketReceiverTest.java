package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.util.IListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * <p>
 *     This class contains all tests for {@link UnixSocketReceiver}
 * </p>
 *
 * <p>
 *     These tests only work if snort is running and are all ignored for unit testing.
 *     Further investigation is needed to check if automated unit tests might be possible.
 * </p>
 *
 * @author Mark Giraud
 */
public class UnixSocketReceiverTest {

    UnixSocketReceiver receiver;
    IFilter mockedFilter;
    Thread testRunner;
    IListener<ITruffleCommand> mockedListener;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        mockedFilter = mock(IFilter.class);
        receiver = new UnixSocketReceiver(mock(INetworkWritingPort.class), mockedFilter);

        mockedListener = (IListener<ITruffleCommand>) mock(IListener.class);
        receiver.addListener(mockedListener);

        testRunner = new Thread(receiver);
        //testRunner.start();
    }

    @After
    public void tearDown() throws Exception {
        testRunner.interrupt();

        mockedFilter = null;
        receiver = null;
        testRunner = null;
    }

    @Ignore
    @Test
    public void testConnect_ifSnortNotRunning() throws Exception {
        ArgumentCaptor<ITruffleCommand> argumentCaptor = ArgumentCaptor.forClass(ITruffleCommand.class);

        receiver.connect();

        verify(mockedListener).receive(argumentCaptor.capture());

    }

    /**
     * This test only works if snort is running and is ignored for travis.
     * Further investigation is needed to check if automated unit tests might be possible.
     *
     * @throws Exception
     */
    @Ignore
    @Test
    public void testReceiver() throws Exception {

        //TODO simulate a running snort and verify if the connect and disconnect process works

        receiver.addListener(System.out::println);

        testRunner.start();

        receiver.connect();

        Thread.sleep(5000);

        receiver.disconnect();
    }
}