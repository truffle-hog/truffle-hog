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
     *     Tests if the replay_log folder is created correctly.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testGetReplayLogFolder() throws Exception {
        boolean fileExists = fileSystem.getReplayLogFolder().exists();
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