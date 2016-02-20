package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.FileSystem;
import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 *     Tests for the SettingsDataModel
 * </p>
 * <p>
 *     Please note some of these tests might fail if the underlying xml file was changed by some other failing tests
 *     in a way it should not have been.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class SettingsDataModelTest {

    private FileSystem fileSystem;
    private ExecutorService executorService;
    private ConfigDataModel configDataModel;

    /**
     * <p>
     *     Sets everything up for every test
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        this.executorService = new LoggedScheduledExecutor(1);
        this.fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(answer -> new File("./src/test/resources/data"));
        when(fileSystem.getConfigFolder()).thenAnswer(answer -> new File("./src/test/resources/data/config"));
        this.configDataModel = new ConfigDataModel(fileSystem, executorService);
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
        executorService = null;
        fileSystem = null;
        configDataModel = null;
    }

    /**
     * <p>
     *     Tests whether the resource file is copied correctly if it does not exist
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testStartWithNoFile() throws Exception{
        this.fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(answer -> new File("./src/test/resources/data"));
        when(fileSystem.getConfigFolder()).thenAnswer(answer -> new File("./src/test/resources/data/config-error-test"));

        if (fileSystem.getConfigFolder().exists()) {
            FileUtils.deleteDirectory(fileSystem.getConfigFolder());
            fileSystem.getConfigFolder().mkdir();
        } else {
            fileSystem.getConfigFolder().mkdir();
        }

        this.configDataModel = new ConfigDataModel(fileSystem, executorService);
        boolean exists = new File(fileSystem.getConfigFolder()  + File.separator + "system_config.xml").exists();

        if (fileSystem.getConfigFolder().exists()) {
            FileUtils.deleteDirectory(fileSystem.getConfigFolder());
            fileSystem.getConfigFolder().mkdir();
        }

        assertEquals(true, exists);
    }

    /**
     * <p>
     *     Tests the loading process.
     * </p>
     * <p>
     *     Please note that this test might fail if the underlying xml file was changed by some failing tests
     *     in a way it should not have been.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testLoad() throws Exception {
        configDataModel.load();
        assertEquals("007", configDataModel.getSetting(Integer.class, "test-integer").getValue());
        assertEquals("hello world", configDataModel.getSetting(String.class, "test-string").getValue());
        assertEquals("true", configDataModel.getSetting(Boolean.class, "test-boolean").getValue());
    }

    /**
     * <p>
     *     Checks if the retrieved values match the expected values and changes some of the values, and then checks if
     *     the file was updated by changing the value back to the original value. This test does this for the integer,
     *     string and boolean entry in the xml file.
     * </p>
     * <p>
     *     Please note that this test might fail if the underlying xml file was changed by some failing tests
     *     in a way it should not have been.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testGet() throws Exception {
        // Test for success
        testGetEntrySuccess(Integer.class, "test-integer", "007", "42");
        testGetEntrySuccess(String.class, "test-string", "hello world", "Trouble Hog");
        testGetEntrySuccess(Boolean.class, "test-boolean", "true", "false");


        // Test for error
        StringProperty property1 = configDataModel.getSetting(String.class, "test-integer");
        assertEquals(null, property1);

        StringProperty property2 = configDataModel.getSetting(String.class, "I love ice cream");
        assertEquals(null, property2);

        StringProperty property3 = configDataModel.getSetting(Optional.class, "I love ice cream");
        assertEquals(null, property3);
    }

    /**
     * <p>
     *     Checks if the retrieved values match the expected values and changes some of the values, and then checks if
     *     the file was updated by changing the value back to the original value.
     * </p>
     *
     * @param classType The class of the value
     * @param key The key of the value
     * @param oldValue The starting value to check against
     * @param newValue The new value to check against after is has been set
     * @throws Exception Passes any errors that occurred during the test on
     */
    private void testGetEntrySuccess(Class classType, String key, String oldValue, String newValue) throws Exception {
        configDataModel.load();
        StringProperty property1 = configDataModel.getSetting(classType, key);
        assertEquals(true, property1.getValue().equals(oldValue));

        StringProperty property2 = new SimpleStringProperty(property1.getValue());
        property1.bindBidirectional(property2);

        property2.setValue(newValue);

        Thread.sleep(500); // sleep because the saving to file happens in another thread

        configDataModel.load();
        property1 = configDataModel.getSetting(classType, key);
        assertEquals(true, property1.getValue().equals(newValue));

        property1.setValue(oldValue);

        Thread.sleep(500); // sleep because the saving to file happens in another thread

        configDataModel.load();
        property1 = configDataModel.getSetting(classType, key);
        assertEquals(true, property1.getValue().equals(oldValue));
    }
}