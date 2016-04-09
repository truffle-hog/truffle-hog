package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.model.network.recording.INetworkTape;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Valentin Kiechle on 07.04.2016.
 */
public class StartRecordCommandTest {

    private INetworkDevice networkDevice;
    private INetwork network;
    private INetworkTape networkTape;
    private StartRecordCommand src;

    @Before
    public void setup(){
        networkDevice = mock(INetworkDevice.class);
        network = mock(INetwork.class);
        networkTape = mock(INetworkTape.class);
        src = new StartRecordCommand(networkDevice, network, networkTape);

    }

    @After
    public void tearDown() {
        network = null;
        networkDevice = null;
        networkTape = null;
    }

    @Test
    public void startRecordTest_42FrameRate() {
        when(networkTape.getFrameRate()).thenReturn(42);
        src.execute();
        verify(networkDevice, times(1)).record(network, networkTape, 42);
    }

    @Test
    public void startRecordTest_0FrameRate() {
        when(networkTape.getFrameRate()).thenReturn((0));
        src.execute();
        verify(networkDevice, times(1)).record(network, networkTape, 0);
    }

    @Test
    public void startRecordTest_negativeFrameRate() {
        when(networkTape.getFrameRate()).thenReturn(-1);
        src.execute();
        verify(networkDevice, times(1)).record(network, networkTape, -1);
        //TODO test for negative framerate??
    }

}