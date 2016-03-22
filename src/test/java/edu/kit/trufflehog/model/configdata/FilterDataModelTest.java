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
import edu.kit.trufflehog.model.filter.FilterOrigin;
import edu.kit.trufflehog.model.filter.SelectionModel;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 *     This class tests the functionality of the FilterDataModel. It test add, remove, update, load and get functions of
 *     the FilterDataModel class.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterDataModelTest {
    private FileSystem fileSystem;
    private FilterDataModel filterDataModel;
    private File databaseFile;

    /**
     * <p>
     *     Sets everything up for every test
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        this.databaseFile = new File("./src/test/resources/data/config/filters.sql");
        this.fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(answer -> new File("./src/test/resources/data"));
        when(fileSystem.getConfigFolder()).thenAnswer(answer -> new File("./src/test/resources/data/config"));
        this.filterDataModel = new FilterDataModel(fileSystem);
    }

    /**
     * <p>
     *     Ends everything after the test
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @After
    public void tearDown() throws Exception {
        filterDataModel.close();
        if (databaseFile.exists()) {
            FileUtils.forceDelete(databaseFile);
        }
    }

    /**
     * <p>
     *     Tests the update functionality of the FilterDataModel by adding FilterInputs, and then editing them, and then
     *     calling the update function on the database (which removes and adds them again).
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    // FIXME fix this test, it randomly fails (Database file locked)
    @Test
    public void testUpdateAndLoadFilterInDatabase() throws Exception {
        List<FilterInput> filterInputs = new ArrayList<>();
        int size = (int) (Math.random() * 50);

        // Add X FilterInputs into the database
        for (int i = 0; i < size; i++) {
            FilterInput filterInput = generateRandomFilterInput();
            filterInputs.add(filterInput);
            filterDataModel.addFilterToDatabaseAsynchronous(filterInput);
        }

        // Wait for all threads to finish
        Thread.sleep(5000);

        // Retrieve them
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters()
                .stream().collect(Collectors.toMap(FilterInput::getName, Function.identity()));

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

        // Wait for all threads to finish
        Thread.sleep(5000);

        // Retrieve them
        filterDataModel = new FilterDataModel(fileSystem);
        filterInputFromDB = filterDataModel.getAllFilters()
                .stream().collect(Collectors.toMap(FilterInput::getName, Function.identity()));

        // Make sure we could retrieve them all correctly
        for (FilterInput filterInput : updatedFilterInputs) {
            assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
        }
    }

    /**
     * <p>
     *     Test the add functionality of the database by generating 100 random FilterInputs and adding them to the
     *     database and then checking if they were all added correctly.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    // FIXME fix this test, it randomly fails (Database file locked)
    @Test
    public void testAddAndLoadFilterToDatabase() throws Exception {
        List<FilterInput> filterInputs = new ArrayList<>();
        int size = (int) (Math.random() * 50);

        // Add X FilterInputs into the database
        for (int i = 0; i < size; i++) {
            FilterInput filterInput = generateRandomFilterInput();
            filterInputs.add(filterInput);
            filterDataModel.addFilterToDatabaseAsynchronous(filterInput);
        }

        // Wait for all threads to finish
        Thread.sleep(5000);

        // Retrieve them
        filterDataModel = new FilterDataModel(fileSystem);
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters()
                .stream().collect(Collectors.toMap(FilterInput::getName, Function.identity()));

        // Make sure we could retrieve them all correctly
        for (FilterInput filterInput : filterInputs) {
            assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
        }
    }

    /**
     * <p>
     *     Tests the remove functionality of the database by adding 100 randomly generated FilterInputs and removing
     *     them again, making sure the database is up-to-date along the way.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    // FIXME fix this test, it randomly fails (Database file locked)
    @Test
    public void testRemoveFilterFromDatabase() throws Exception {
        List<FilterInput> filterInputs = new ArrayList<>();
        int size = (int) (Math.random() * 50);

        // Add X FilterInputs into the database
        for (int i = 0; i < size; i++) {
            FilterInput filterInput = generateRandomFilterInput();
            filterInputs.add(filterInput);
            filterDataModel.addFilterToDatabaseAsynchronous(filterInput);
        }

        // Wait for all threads to finish
        Thread.sleep(5000);

        // Retrieve them
        filterDataModel = new FilterDataModel(fileSystem);
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters()
                .stream().collect(Collectors.toMap(FilterInput::getName, Function.identity()));

        // Make sure we could retrieve them all correctly
        for (FilterInput filterInput : filterInputs) {
            assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
        }

        // Delete them
        filterInputFromDB.forEach((name, value) -> filterDataModel.removeFilterFromDatabaseAsynchronous(value));

        // Wait for all threads to finish
        Thread.sleep(5000);

        // Retrieve them
        filterDataModel = new FilterDataModel(fileSystem);
        filterInputFromDB = filterDataModel.getAllFilters()
                .stream().collect(Collectors.toMap(FilterInput::getName, Function.identity()));

        // Make sure none were found
        assertEquals(true, filterInputFromDB.isEmpty());
    }

    /**
     * <p>
     *     Tests the database for correct duplicate entry handling. The same entry is added twice, but should only
     *     be there once.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testForDuplicateEntry() throws Exception {
        FilterInput filterInput = generateRandomFilterInput();
        filterDataModel.addFilterToDatabaseAsynchronous(filterInput);

        // Wait for 1st filter to be added
        Thread.sleep(1000);

        filterDataModel.addFilterToDatabaseAsynchronous(filterInput);

        // Wait for all threads to finish
        Thread.sleep(1000);

        // Retrieve them
        filterDataModel = new FilterDataModel(fileSystem);
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters()
                .stream().collect(Collectors.toMap(FilterInput::getName, Function.identity()));

        assertEquals(1, filterInputFromDB.size());
        assertEquals(true, filterInputFromDB.containsKey(filterInput.getName()));
    }

    /**
     * <p>
     *     Tests the database for correct null entry handling. Nothing should be added to the database when a null
     *     element is passed into the list.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testForNullEntry() throws Exception {
        filterDataModel.addFilterToDatabaseAsynchronous(null);

        // Wait for all threads to finish
        Thread.sleep(1000);

        // Retrieve them
        filterDataModel = new FilterDataModel(fileSystem);
        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters()
                .stream().collect(Collectors.toMap(FilterInput::getName, Function.identity()));

        assertEquals(0, filterInputFromDB.size());
    }

    /**
     * <p>
     *     Adds a randomly generated filterInput into the database and retrieves it with the get method to test if it
     *     works properly.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testGet() throws Exception {
        FilterInput filterInput = generateRandomFilterInput();
        filterDataModel.addFilterToDatabaseAsynchronous(filterInput);

        // Wait for all threads to finish
        Thread.sleep(1000);

        // Retrieve them
        filterDataModel = new FilterDataModel(fileSystem);

        Map<String, FilterInput> filterInputFromDB = filterDataModel.getAllFilters()
                .stream().collect(Collectors.toMap(FilterInput::getName, Function.identity()));

        // Make sure they are equal
        assertEquals(filterInput.getName(), filterInputFromDB.get(filterInput.getName()).getName());
        assertEquals(filterInput.getRules(), filterInputFromDB.get(filterInput.getName()).getRules());
        assertEquals(filterInput.getColor(), filterInputFromDB.get(filterInput.getName()).getColor());
    }

    /**
     * <p>
     *     Generates a single random FilterInput object (that means, random name, random rules and random color).
     * </p>
     *
     * @return A randomly generated FilterInput object.
     */
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

        int priority = (int) (Math.random() * 1000);

        // Generate FilterInput object
        return new FilterInput(name, SelectionModel.SELECTION, FilterOrigin.IP, rules, color, true, priority);
    }

    /**
     * <p>
     *     Randomly updates an existing FilterInput object. That means the color and the rules change, but the name
     *     stays the same.
     * </p>
     *
     * @param filterInput The filterInput to update.
     * @return the updated filterInput object.
     */
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

        int priority = (int) (Math.random() * 1000);

        return new FilterInput(filterInput.getName(), SelectionModel.SELECTION, FilterOrigin.MAC, rules, color,
                false, priority);
    }

    /**
     * <p>
     *     Generates a random string of the given length that can contain all characters between "a-z0-9 ".
     * </p>
     *
     * @param length The length that the generated string should have.
     * @return The randomly generated string.
     */
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