package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.MacroFilter;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Valentin Kiechle on 07.04.2016.
 * TODO: repair tests!
 */
public class AddFilterCommandTest {
    private MacroFilter macroFilter;
    private INetworkIOPort nwp;
    private FilterInput filterInput;
    private UpdateFilterCommand ufc;

    @Before
    public void setup() {

        // TODO change?
/*        macroFilter = mock(MacroFilter.class);
        nwp = mock(INetworkIOPort.class);
        //ufc = new UpdateFilterCommand(nwp, macroFilter);
        filterInput = mock(FilterInput.class);
        ufc.setSelection(filterInput);
        when(filterInput.isActive()).thenReturn(true);
        when(filterInput.isDeleted()).thenReturn(false);*/
    }

    @After
    public void teardown() {
        macroFilter = null;
        nwp = null;
        ufc = null;
        filterInput = null;
    }

    //TODO testing i guess
}