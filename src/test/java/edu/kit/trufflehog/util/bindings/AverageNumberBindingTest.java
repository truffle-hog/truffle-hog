package edu.kit.trufflehog.util.bindings;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * \brief
 * \details
 * \date 14.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class AverageNumberBindingTest {

    private static final Logger logger = LogManager.getLogger(AverageNumberBindingTest.class);

    private int intValues[];

    private int average;

    private List<IntegerProperty> values;

    private AverageNumberBinding binding;

    private int safeComputeAverage(int ...values) {

        int average = 0;

        for (int value : values) {
            average += value;
        }
        return average / values.length;
    }

    private List<IntegerProperty> getValuesAsPropertyList(int ...values) {

        final ArrayList<IntegerProperty> list = new ArrayList<>();

        for (int value : values) {
            list.add(new SimpleIntegerProperty(value));
        }
        return list;
    }

    @Before
    public void setUp() {

        intValues = new int[]{0, 5, 10, 200, 300};
        average = safeComputeAverage(intValues);
        values = getValuesAsPropertyList(intValues);
        binding = new AverageNumberBinding();

    }

    @After
    public void tearDown() {
        intValues = null;
        average = 0;
        values = null;
        binding = null;
    }

    @Test
    public void testBind() throws Exception {

    }

    @Test
    public void testBindAll() throws Exception {

        binding.bindAll(values);
        Assert.assertEquals(safeComputeAverage(intValues), binding.getValue().intValue());

        values.get(0).setValue(50);
        intValues[0] = 50;
        Assert.assertEquals(safeComputeAverage(intValues), binding.getValue().intValue());

        int newValues[] = {0, 20, 40};
        binding.bindAll(getValuesAsPropertyList(newValues));

        intValues = new int[]{50, 5, 10, 200, 300, 0, 20, 40};
        Assert.assertEquals(safeComputeAverage(intValues), binding.getValue().intValue());
    }

    @Test
    public void testUnbindAll() throws Exception {

        binding.bindAll(values);
        Assert.assertEquals(safeComputeAverage(intValues), binding.getValue().intValue());

        binding.unbindAll();
        Assert.assertEquals(0, binding.getValue().intValue());

    }

    @Test
    public void testUnbind() throws Exception {

        binding.bindAll(values);
        Assert.assertEquals(safeComputeAverage(intValues), binding.getValue().intValue());

        binding.unbind(values.get(0));
        intValues = new int[]{5,10,200,300};
        Assert.assertEquals(safeComputeAverage(intValues), binding.getValue().intValue());
    }
}