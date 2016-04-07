package edu.kit.trufflehog.interaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * ProtocolControlInteractionTest class
 */
public class ProtocolControlInteractionTest {

    /**
     * Checks if ProtocolControlInteractions are available and not arbitrarily modified.
     */
    @Test
    public void testAvailability() {
        assertNotNull(ProtocolControlInteraction.CONNECT);
        assertNotNull(ProtocolControlInteraction.DISCONNECT);
        assertNotNull(ProtocolControlInteraction.RECORD_START);
        assertNotNull(ProtocolControlInteraction.RECORD_STOP);
        assertNotNull(ProtocolControlInteraction.REFRESH_LAYOUT);
    }


}
