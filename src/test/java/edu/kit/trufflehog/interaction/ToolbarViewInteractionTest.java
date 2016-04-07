package edu.kit.trufflehog.interaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * ToolbarViewInteractionTest class
 */
public class ToolbarViewInteractionTest {

    /**
     * Checks if ToolbarViewInteractions are available and not arbitrarily modified.
     */
    @Test
    public void testAvailability() {
        assertNotNull(ToolbarViewInteraction.CONNECT);
        assertNotNull(ToolbarViewInteraction.DISCONNECT);
    }


}
