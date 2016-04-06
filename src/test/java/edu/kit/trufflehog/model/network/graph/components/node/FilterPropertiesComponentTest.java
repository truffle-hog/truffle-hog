package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.filter.IPAddressFilter;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Hoehler on 25.03.2016.
 */
public class FilterPropertiesComponentTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void addFilterColor() throws Exception {
        Color color = Color.CYAN;
        FilterPropertiesComponent component = new FilterPropertiesComponent();
        IFilter filter = Mockito.mock(IPAddressFilter.class);
        component.addFilterColor(filter, color);

        assertTrue(component.getFilterColor().getBlue() == Color.CYAN.getBlue());
        assertEquals(1, component.getFilterColors().size());
    }

    @Test
    public void getFilterColors() throws Exception {
        FilterPropertiesComponent component = new FilterPropertiesComponent();
        IFilter filter1 = Mockito.mock(IPAddressFilter.class);
        IFilter filter2 = Mockito.mock(IPAddressFilter.class);
        IFilter filter3 = Mockito.mock(IPAddressFilter.class);
        Color color1 = Color.CYAN;
        Color color2 = Color.MAGENTA;
        Color color3 = Color.GREEN;

        component.addFilterColor(filter1, color1);
        component.addFilterColor(filter2, color2);
        component.addFilterColor(filter3, color3);

        assertEquals(3, component.getFilterColors().size());
        assertTrue(component.getFilterColors().containsEntry(filter1, Color.CYAN));
        assertTrue(component.getFilterColors().containsEntry(filter2, color2));
        assertTrue(component.getFilterColors().containsEntry(filter3, color3));
    }

    @Test
    public void removeFilterColor() throws Exception {
        FilterPropertiesComponent component = new FilterPropertiesComponent();
        IFilter filter1 = Mockito.mock(IPAddressFilter.class);
        when(filter1.getFilterColor()).thenReturn(Color.CYAN);

        component.addFilterColor(filter1, Color.CYAN);

        assertEquals(1, component.getFilterColors().size());

        component.removeFilterColor(filter1);

        assertEquals(0, component.getFilterColors().size());
    }

    @After
    public void tearDown() throws Exception {

    }
}