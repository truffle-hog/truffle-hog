package edu.kit.trufflehog.interaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * StartViewInteractionTest class
 */
public class StartViewInteractionTest {

    /**
     * Checks if StartViewInteractions are available and not arbitrarily modified.
     */
    @Test
    public void testAvailability() {
        assertNotNull(StartViewInteraction.START_CAPTURE);
        assertNotNull(StartViewInteraction.START_DEMO);
        assertNotNull(StartViewInteraction.START_PROFINET);
    }


}
