package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.filter.MacroFilter;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Valentin Kiechle on 05.04.2016.
 */
public class UpdateFilterCommandTest {
    private MacroFilter macroFilter;
    private INetworkIOPort nwp;
    private FilterInput filterInput;
    private Map<FilterInput, IFilter> filterMap = new HashMap<>();
    private UpdateFilterCommand ufc;

    @Before
    public void setup() {
        macroFilter = mock(MacroFilter.class);
        nwp = mock(INetworkIOPort.class);
        ufc = new UpdateFilterCommand(nwp, macroFilter);
    }

    @After
    public void teardown() {
        macroFilter = null;
        nwp = null;
        ufc = null;
    }


    @Test
    public void updateFilterTest() {
        FilterInput testInput = mock(FilterInput.class);
        ufc.setSelection(testInput);
        assert(true);
//TODO
    }

}