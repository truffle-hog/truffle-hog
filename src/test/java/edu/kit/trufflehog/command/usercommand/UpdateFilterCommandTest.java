package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.*;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.kit.trufflehog.model.network.MacAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
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
    private UpdateFilterCommand ufc;

    @Before
    public void setup() {
        macroFilter = mock(MacroFilter.class);
        nwp = mock(INetworkIOPort.class);
        ufc = new UpdateFilterCommand(nwp, macroFilter);
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
        ufc = new UpdateFilterCommand(nwp, null);
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
        ufc = new UpdateFilterCommand(null, null);
    }

    @Test
    public void updateMACFilterCommandTest() {
        when(filterInput.getOrigin()).thenReturn(FilterOrigin.MAC);
        ufc.execute();
        verify(nwp, times(1)).applyFilter(any(MACAddressFilter.class));
        verify(macroFilter, times(1)).addFilter(any(MACAddressFilter.class));
    }

    @Test
    public void updateIPFilterCommandTest() {
        when(filterInput.getOrigin()).thenReturn(FilterOrigin.IP);
        ufc.execute();
        verify(nwp, times(1)).applyFilter(any(IPAddressFilter.class));
        verify(macroFilter, times(1)).addFilter(any(IPAddressFilter.class));
    }

    @Test
    public void updateNameRegexFilterCommandTest() {
        when(filterInput.getOrigin()).thenReturn(FilterOrigin.NAME);
        ufc.execute();
        verify(nwp, times(1)).applyFilter(any(NameRegexFilter.class));
        verify(macroFilter, times(1)).addFilter(any(NameRegexFilter.class));
    }

    @Test
    public void updateOldExistingFilterTest() {
        when(filterInput.getOrigin()).thenReturn(FilterOrigin.NAME);
        ufc.execute();
        when(filterInput.getOrigin()).thenReturn(FilterOrigin.IP);
        ufc.execute();
        verify(nwp, times(2)).applyFilter(any(IPAddressFilter.class));
        verify(macroFilter, times(2)).addFilter(any(IPAddressFilter.class));
    }
}