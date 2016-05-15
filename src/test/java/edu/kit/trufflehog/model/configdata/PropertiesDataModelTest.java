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
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 *     Tests the basic functionalities of the property loading and copying mechanism.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class PropertiesDataModelTest {
    private FileSystem fileSystem;
    private PropertiesDataModel propertiesDataModel;

    /**
     * <p>
     *     Sets everything up for every test
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Before
    public void setUp() throws Exception {
        this.fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(answer -> new File("./src/test/resources/data"));
        when(fileSystem.getConfigFolder()).thenAnswer(answer -> new File("./src/test/resources/data/config"));
    }

    /**
     * <p>
     *     Tests whether the english property file can be loaded and whether the test property can be accessed.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    @Ignore
    public void testEn() throws Exception {
        this.propertiesDataModel = new PropertiesDataModel(new Locale("en"), fileSystem);
        String value = propertiesDataModel.get("test_property");
        assertEquals("this is a test", value);
    }

    /**
     * <p>
     *     Tests whether the german property file can be loaded and whether the test property can be accessed.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    @Ignore
    public void testDe() throws Exception {
        this.propertiesDataModel = new PropertiesDataModel(new Locale("de"), fileSystem);
        String value = propertiesDataModel.get("test_property");
        assertEquals("das ist ein test", value);
    }

    /**
     * <p>
     *     Tests whether the property file is copied correctly if it was not run before.
     * </p>
     *
     * @throws Exception Passes any errors that occurred during the test on
     */
    @Test
    public void testWithNoFile() throws Exception {
        this.fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(answer -> new File("./src/test/resources/data"));
        when(fileSystem.getConfigFolder()).thenAnswer(answer -> new File("./src/test/resources/data/config-error-test"));

        if (fileSystem.getConfigFolder().exists()) {
            FileUtils.deleteDirectory(fileSystem.getConfigFolder());
            fileSystem.getConfigFolder().mkdir();
        } else {
            fileSystem.getConfigFolder().mkdir();
        }

        this.propertiesDataModel = new PropertiesDataModel(new Locale("en"), fileSystem);

        boolean exists = new File(fileSystem.getConfigFolder() + File.separator + "system_properties_en.properties")
                .exists();

        if (fileSystem.getConfigFolder().exists()) {
            FileUtils.deleteDirectory(fileSystem.getConfigFolder());
            fileSystem.getConfigFolder().mkdir();
        }

        assertEquals(true, exists);
    }
}