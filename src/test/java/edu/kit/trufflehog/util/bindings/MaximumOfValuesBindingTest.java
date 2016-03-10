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
public class MaximumOfValuesBindingTest {

    private MaximumOfValuesBinding binding;

    @Before
    public void setUp() throws Exception {

        binding = new MaximumOfValuesBinding();
    }

    //FIXME: Fix this whole test class and the Binding.
    @Ignore
    @Test
    public void testBindProperty() throws Exception {

        final IntegerProperty testBinded = new SimpleIntegerProperty(0);

        final PlatformIntegerBinding platBinded = new PlatformIntegerBinding(testBinded);

        testBinded.bind(binding);

        final IntegerProperty int1 = new SimpleIntegerProperty(0);
        final IntegerProperty int2 = new SimpleIntegerProperty(5);
        final IntegerProperty int3 = new SimpleIntegerProperty(2);
        final IntegerProperty int4 = new SimpleIntegerProperty(10);

        binding.bindProperty(int1);
        assertEquals(0, platBinded.get());

        binding.bindProperty(int2);
        assertEquals(5, platBinded.get());

        binding.bindProperty(int3);
        assertEquals(5, platBinded.get());

        binding.bindProperty(int4);
        assertEquals(10, platBinded.get());

        int1.set(20);
        assertEquals(20, platBinded.get());
    }

    @Test
    public void testComputeValue() throws Exception {

    }

    @Test
    public void testOnChanged() throws Exception {

    }

    @Test
    public void testChanged() throws Exception {

    }
}