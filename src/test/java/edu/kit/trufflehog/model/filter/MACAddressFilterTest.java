package edu.kit.trufflehog.model.filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 *     This class contains all tests for the {@link MACAddressFilter} class.
 * </p>
 *
 * @author Mark Giraud
 */
public class MACAddressFilterTest {

    private MACAddressFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new MACAddressFilter();
    }

    @After
    public void tearDown() throws Exception {
        filter = null;
    }

    @Test(expected = InvalidFilterRule.class)
    public void addInput_throws_on_invalid_rules() throws Exception {
        List<String> ruleList = new LinkedList<>();
        ruleList.add("AA:BB:C:DD:EE:F");
        ruleList.add("AA:XX:CC:DD:EE:FF");

        FilterInput filterInput = mock(FilterInput.class);
        when(filterInput.getRules()).thenReturn(ruleList);

        filter.addInput(filterInput);
    }
}