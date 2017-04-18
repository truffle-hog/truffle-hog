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

package edu.kit.trufflehog.model;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * <p>
 *     Unit tests for the {@link FileSystem} class
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FileSystemTest {
    private FileSystem fileSystem;

    /**
     * <p>
     *     Resets the {@link FileSystem} object at the beginning of every test.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        fileSystem = new FileSystem();
    }

    /**
     * <p>
     *     Deletes all folders created by the previous test.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @After
    public void tearDown() throws Exception {
        if (fileSystem.getDataFolder().exists()) {
            FileUtils.deleteDirectory(fileSystem.getDataFolder());
        }
    }

    /**
     * <p>
     *     Tests if the data folder is created correctly.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testGetDataFolder() throws Exception {
        boolean fileExists = fileSystem.getDataFolder().exists();
        assertEquals(fileExists, true);
    }

    /**
     * <p>
     *     Tests if the config folder is created correctly.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testGetConfigFolder() throws Exception {
        boolean fileExists = fileSystem.getConfigFolder().exists();
        assertEquals(fileExists, true);
    }

    /**
     * <p>
     *     Tests if the filter folder is created correctly.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testGetFilterFolder() throws Exception {
        boolean fileExists = fileSystem.getFilterFolder().exists();
        assertEquals(fileExists, true);
    }

    /**
     * <p>
     *     Tests if the truffle_data_log folder is created correctly.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testGetTruffleDataLogFolder() throws Exception {
        boolean fileExists = fileSystem.getTruffleDataLogFolder().exists();
        assertEquals(fileExists, true);
    }
}