/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.model.filter.FilterInput;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterDataModelTest {
    private FileSystem fileSystem;
    private FilterDataModel filterDataModel;

    @Before
    public void setUp() throws Exception {
        this.fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(answer -> new File("./src/test/resources/data"));
        when(fileSystem.getConfigFolder()).thenAnswer(answer -> new File("./src/test/resources/data/config"));
        this.filterDataModel = new FilterDataModel(fileSystem);
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(fileSystem.getConfigFolder());
        fileSystem.getConfigFolder().mkdir();
    }

    @Test
    public void testUpdateAndLoadFilterInDatabase() throws Exception {
        List<FilterInput> filterInputs = new ArrayList<>();
        int size = (int) (Math.random() * 100);

        // Add X FilterInputs into the database
        for (int i = 0; i < size; i++) {
            FilterInput filterInput = generateRandomFilterInput();
            filterInputs.add(filterInput);
            filterDataModel.addFilterToDatabase(filterInput);
        }

        // Retrieve them
        filterDataModel.load();
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters();

        // Make sure we could retrieve them all correctly
        for (FilterInput filterInput : filterInputs) {
            assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
        }

        // Update them
        List<FilterInput> updatedFilterInputs = new ArrayList<>();
        for (FilterInput filterInput : filterInputs) {
            updatedFilterInputs.add(updateFilterInput(filterInput));
            filterDataModel.updateFilterInDatabase(filterInput);
        }

        // Retrieve them
        filterDataModel.load();
        filterInputFromDB = filterDataModel.getAllFilters();

        // Make sure we could retrieve them all correctly
        for (FilterInput filterInput : updatedFilterInputs) {
            assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
        }
    }

    @Test
    public void testAddAndGetFilterToDatabase() throws Exception {
        List<FilterInput> filterInputs = new ArrayList<>();
        int size = (int) (Math.random() * 100);

        // Add X FilterInputs into the database
        for (int i = 0; i < size; i++) {
            FilterInput filterInput = generateRandomFilterInput();
            filterInputs.add(filterInput);
            filterDataModel.addFilterToDatabase(filterInput);
        }

        // Retrieve them
        filterDataModel.load();
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters();

        // Make sure we could retrieve them all correctly
        for (FilterInput filterInput : filterInputs) {
            assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
        }    }

    @Test
    public void testRemoveFilterFromDatabase() throws Exception {
        List<FilterInput> filterInputs = new ArrayList<>();
        int size = (int) (Math.random() * 100);

        // Add X FilterInputs into the database
        for (int i = 0; i < size; i++) {
            FilterInput filterInput = generateRandomFilterInput();
            filterInputs.add(filterInput);
            filterDataModel.addFilterToDatabase(filterInput);
        }

        // Retrieve them
        filterDataModel.load();
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters();

        // Make sure we could retrieve them all correctly
        for (FilterInput filterInput : filterInputs) {
            assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
        }

        // Delete them
        filterInputFromDB.forEach((name, value) -> filterDataModel.removeFilterFromDatabase(value));

        // Retrieve them
        filterDataModel.load();
        filterInputFromDB = filterDataModel.getAllFilters();

        // Make sure none were found
        assertEquals(true, filterInputFromDB.isEmpty());
    }

    @Test
    public void testForDuplicateEntry() throws Exception {
        FilterInput filterInput = generateRandomFilterInput();
        filterDataModel.addFilterToDatabase(filterInput);
        filterDataModel.addFilterToDatabase(filterInput);


        // Retrieve them
        filterDataModel.load();
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters();

        assertEquals(1, filterInputFromDB.size());
        assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
    }

    @Test
    public void testForNullEntry() throws Exception {
        filterDataModel.addFilterToDatabase(null);

        // Retrieve them
        filterDataModel.load();
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters();

        assertEquals(0, filterInputFromDB.size());
    }

    private FilterInput generateRandomFilterInput() {
        // Generate name
        String name = randomString(10);

        // Generate Rules
        List<String> rules = new ArrayList<>();

        int limit = (int) (Math.random() * 100);
        for (int i = 0; i < limit; i++) {
            int ruleSize = (int) (Math.random() * 1000);
            rules.add(randomString(ruleSize));
        }

        // Generate color;
        int color_r = (int) (Math.random() * 255);
        int color_g = (int) (Math.random() * 255);
        int color_b = (int) (Math.random() * 255);
        int color_a = (int) (Math.random() * 255);
        Color color = new Color(color_r, color_g, color_b, color_a);

        // Generate FilterInput object
        return new FilterInput(name, rules, color);
    }

    private FilterInput updateFilterInput(FilterInput filterInput) {
        // Generate Rules
        List<String> rules = new ArrayList<>();

        int limit = (int) (Math.random() * 100);
        for (int i = 0; i < limit; i++) {
            int ruleSize = (int) (Math.random() * 1000);
            rules.add(randomString(ruleSize));
        }

        // Generate color;
        int color_r = (int) (Math.random() * 255);
        int color_g = (int) (Math.random() * 255);
        int color_b = (int) (Math.random() * 255);
        int color_a = (int) (Math.random() * 255);
        Color color = new Color(color_r, color_g, color_b, color_a);

        return new FilterInput(filterInput.getName(), rules, color);
    }

    private String randomString(int length) {
        String charactersInName = "abcdefghijklmnopqrst0123456789 ";
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = charactersInName.charAt(rng.nextInt(charactersInName.length()));
        }
        return  new String(text);
    }

}