package edu.kit.trufflehog.viewmodel;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterType;
import edu.kit.trufflehog.model.filter.SelectionModel;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Hoehler on 19.04.2016.
 */
public class FilterViewModelTest {
    @Test
    public void updateFilter() throws Exception {
        ConfigData data = Mockito.mock(ConfigData.class);
        FilterViewModel model = new FilterViewModel(data);
        when(data.getAllLoadedFilters()).thenReturn(FXCollections.emptyObservableList());
        when(data.getProperty("SELECTION_LABEL")).thenReturn(SelectionModel.SELECTION.toString());
        when(data.getProperty("INVERSE_SELECTION_LABEL")).thenReturn(SelectionModel.INVERSE_SELECTION.toString());
        when(data.getProperty("NAME_LABEL")).thenReturn(FilterType.NAME.toString());

        //not gonna mock this as only getter and setter are used
        FilterInput filter = new FilterInput("a", SelectionModel.SELECTION, FilterType.NAME, new LinkedList<String>(),
                Color.ALICEBLUE, false, 10);

        FilterInput updatedFilter = model.updateFilter(filter, "b", SelectionModel.INVERSE_SELECTION.toString(),
                FilterType.NAME.toString(), Color.RED, "12", "null", true);

        assertEquals("b", updatedFilter.getName());
        assertEquals(SelectionModel.INVERSE_SELECTION, updatedFilter.getSelectionModel());
        assertEquals(FilterType.NAME, updatedFilter.getType());
        assertEquals(Color.RED, updatedFilter.getColor());
        assertEquals(12, updatedFilter.getPriority());
        assertEquals(true, updatedFilter.getLegalProperty().get());
    }

    @Test
    public void createFilterInput() throws Exception {
        ConfigData data = Mockito.mock(ConfigData.class);
        FilterViewModel model = new FilterViewModel(data);
        when(data.getAllLoadedFilters()).thenReturn(FXCollections.emptyObservableList());
        when(data.getProperty("SELECTION_LABEL")).thenReturn(SelectionModel.SELECTION.toString());
        when(data.getProperty("INVERSE_SELECTION_LABEL")).thenReturn(SelectionModel.INVERSE_SELECTION.toString());
        when(data.getProperty("NAME_LABEL")).thenReturn(FilterType.NAME.toString());

        FilterInput filter = model.createFilterInput("a", SelectionModel.SELECTION.toString(), FilterType.NAME.toString(),
                Color.ALICEBLUE, "10", "12345", false);

        assertNotNull(filter);
        assertEquals("a", filter.getName());
        assertEquals(SelectionModel.SELECTION, filter.getSelectionModel());
        assertEquals(FilterType.NAME, filter.getType());
        assertEquals(Color.ALICEBLUE, filter.getColor());
        assertEquals(10, filter.getPriority());
        assertEquals(false, filter.getLegalProperty().get());
    }
}