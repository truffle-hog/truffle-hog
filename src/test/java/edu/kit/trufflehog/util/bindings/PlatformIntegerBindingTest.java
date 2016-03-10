package edu.kit.trufflehog.util.bindings;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * \brief
 * \details
 * \date 04.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class PlatformIntegerBindingTest {

    PlatformIntegerBinding binding;

    IntegerProperty tester;

    @Before
    public void setUp() throws Exception {

        tester = new SimpleIntegerProperty(0);
        binding = new PlatformIntegerBinding(tester);
    }

    @Ignore
    @Test
    public void testChanged() throws Exception {
        tester.setValue(5);
        assertEquals(5, binding.get());
    }

    @Test
    public void testComputeValue() throws Exception {

    }
}