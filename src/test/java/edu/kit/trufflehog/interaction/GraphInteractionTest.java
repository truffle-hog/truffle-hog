package edu.kit.trufflehog.interaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * GraphInteraction Test class
 */
public class GraphInteractionTest {

    /**
     * Checks if GraphInteractions are available and not arbitrarily modified.
     */
    @Test
    public void testAvailability() {
        assertNotNull(GraphInteraction.SELECTION);
        assertNotNull(GraphInteraction.VERTEX_SELECTED);
    }


}
