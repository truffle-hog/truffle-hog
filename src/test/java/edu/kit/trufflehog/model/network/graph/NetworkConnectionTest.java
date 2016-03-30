package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.MacAddress;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Hoehler on 25.03.2016.
 */
public class NetworkConnectionTest {
    @Test
    public void equals() throws Exception {
        NetworkNode src = Mockito.mock(NetworkNode.class);
        NetworkNode dest = Mockito.mock(NetworkNode.class);
        when(src.getAddress()).thenReturn(new MacAddress(100));
        when(dest.getAddress()).thenReturn(new MacAddress(101));

        NetworkConnection connection1 = new NetworkConnection(src, dest);
        NetworkConnection connection2 = new NetworkConnection(src, dest);

        assertTrue(connection1.equals(connection1));
        assertTrue(connection1.equals(connection2));
    }
}