package edu.kit.trufflehog.interaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * ToolbarInteractionTest class
 */
public class ToolbarInteractionTest {

    /**
     * Checks if ToolbarViewInteractions are available and not arbitrarily modified.
     */
    @Test
    public void testAvailability() {
        assertNotNull(ToolbarInteraction.CONNECT);
        assertNotNull(ToolbarInteraction.DISCONNECT);
    }


}
