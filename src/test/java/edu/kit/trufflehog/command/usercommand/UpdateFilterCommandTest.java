package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.*;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Valentin Kiechle on 05.04.2016.
 */
public class UpdateFilterCommandTest {
    private MacroFilter macroFilter;
    private INetworkIOPort nwp;
    private FilterInput filterInput;
    private ConfigData configData;
    private UpdateFilterCommand ufc;
    private Map<FilterInput, IFilter> filterMap;

    @Before
    public void setup() {

       // final ConfigData configData, final INetworkIOPort nwp, final MacroFilter macroFilter, final Map<FilterInput, IFilter> filterMap)

        configData = mock(ConfigData.class);
        macroFilter = mock(MacroFilter.class);
        nwp = mock(INetworkIOPort.class);
        filterMap = mock(Map.class);


        ufc = new UpdateFilterCommand(configData, nwp, macroFilter, filterMap);
        filterInput = mock(FilterInput.class);
        ufc.setSelection(filterInput);
        when(filterInput.isActive()).thenReturn(true);
        when(filterInput.isDeleted()).thenReturn(false);
    }

    @After
    public void teardown() {
        macroFilter = null;
        nwp = null;
        ufc = null;
        filterInput = null;
    }


    @Test (expected = NullPointerException.class)
    public void updateNullMacroFilterTest() {

        ufc = new UpdateFilterCommand(configData, nwp, null, filterMap);
    }

    // TODO undeprecate
    @Ignore
    @Test (expected = NullPointerException.class)
    public void updateNullConfigDataTest() {

        ufc = new UpdateFilterCommand(null, nwp, macroFilter, filterMap);
    }

    @Test (expected = NullPointerException.class)
    public void updateNullFilterMapTest() {

        ufc = new UpdateFilterCommand(configData, nwp, macroFilter, null);
    }



    @Test
    public void updateFilterCommandTest_selectingNullFilterShouldWork () {
        try {
            ufc.setSelection(null);
        } catch (NullPointerException e) {
            fail();
        }
        assertTrue(true);
    }

    @Test (expected = NullPointerException.class)
    public void updateFilterCommandTest_NullInitialization () {

        ufc = new UpdateFilterCommand(null, null, null, null);
    }

    @Test
    public void updateMACFilterCommandTest() {
        when(filterInput.getType()).thenReturn(FilterType.MAC);
        ufc.execute();
        verify(nwp, times(1)).applyFilter(any(MACAddressFilter.class));
        verify(macroFilter, times(1)).addFilter(any(MACAddressFilter.class));
    }

    @Test
    public void updateIPFilterCommandTest() {
        when(filterInput.getType()).thenReturn(FilterType.IP);
        ufc.execute();
        verify(nwp, times(1)).applyFilter(any(IPAddressFilter.class));
        verify(macroFilter, times(1)).addFilter(any(IPAddressFilter.class));
    }

    @Test
    public void updateNameRegexFilterCommandTest() {
        when(filterInput.getType()).thenReturn(FilterType.NAME);
        ufc.execute();
        verify(nwp, times(1)).applyFilter(any(NameRegexFilter.class));
        verify(macroFilter, times(1)).addFilter(any(NameRegexFilter.class));
    }

    @Test
    public void updateOldExistingFilterTest() {
        when(filterInput.getType()).thenReturn(FilterType.NAME);
        ufc.execute();
        when(filterInput.getType()).thenReturn(FilterType.IP);
        ufc.execute();
        verify(nwp, times(2)).applyFilter(any(IPAddressFilter.class));
        verify(macroFilter, times(2)).addFilter(any(IPAddressFilter.class));
    }
}