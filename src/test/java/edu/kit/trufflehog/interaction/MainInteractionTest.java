package edu.kit.trufflehog.interaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * MainInteraction Test class
 */
public class MainInteractionTest {

    /**
     * Checks if MainInteractions are available and not arbitrarily modified.
     */
    @Test
    public void testAvailability() {
        assertNotNull(MainInteraction.EXIT);
    }


}
