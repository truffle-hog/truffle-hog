package edu.kit.trufflehog.model.filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.util.LinkedList;

/**
 * <p>
 *     This class contains all tests for the {@link IPAddressFilter} class
 * </p>
 *
 * @author Mark Giraud
 */
public class IPAddressFilterTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void bla() throws Exception {
        java.util.List<String> rules = new LinkedList<>();


        FilterInput filterInput = new FilterInput("test", SelectionModel.SELECTION, FilterOrigin.IP, rules,
                new javafx.scene.paint.Color(0.0, 0.0, 0.0, 0.0), true, 1);
    }
}