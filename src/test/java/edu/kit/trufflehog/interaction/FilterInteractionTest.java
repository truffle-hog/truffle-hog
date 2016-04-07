package edu.kit.trufflehog.interaction;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * FilterInteraction Test class
 */
public class FilterInteractionTest {

    /**
     * Checks if FilterInteractions are available and not arbitrarily modified.
     */
    @Test
    public void testAvailability() {
        assertNotNull(FilterInteraction.ADD);
        assertNotNull(FilterInteraction.REMOVE);
        assertNotNull(FilterInteraction.UPDATE);
    }


}
