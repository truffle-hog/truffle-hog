package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.model.network.NetworkIOPort;
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

    NetworkIOPort networkIOPortMock;

    @Before
    public void setUp() throws Exception {
        networkIOPortMock = mock(NetworkIOPort.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = InvalidFilterRule.class)
    public void addInput_throws_on_invalid_rules() throws Exception {
        List<String> ruleList = new LinkedList<>();
        ruleList.add("AA:BB:C:DD:EE:F");
        ruleList.add("AA:XX:CC:DD:EE:FF");

        FilterInput filterInput = mock(FilterInput.class);
        when(filterInput.getRules()).thenReturn(ruleList);

        new MACAddressFilter(networkIOPortMock, filterInput);
    }

    @Test
    public void compareTo_low_priority_filter_is_less_than_high_priority_filter() throws Exception {
        IFilter lowPrio = new MACAddressFilter(networkIOPortMock, new FilterInput("lowPrio", SelectionModel.SELECTION,
                FilterOrigin.MAC, new LinkedList<>(), null, true, 0));
        IFilter highPrio = new MACAddressFilter(networkIOPortMock, new FilterInput("highPrio", SelectionModel.SELECTION,
                FilterOrigin.MAC, new LinkedList<>(), null, true, 10));

        assertTrue("lowPrio should be less than highPrio", lowPrio.compareTo(highPrio) < 0);
        assertTrue("highPrio should be greater than lowPrio", highPrio.compareTo(lowPrio) > 0);
    }
}