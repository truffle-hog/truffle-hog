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
import org.junit.Test;

import java.io.File;
import java.util.Locale;

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
public class PropertiesDataModelTest {
    private FileSystem fileSystem;
    private PropertiesDataModel propertiesDataModel;

    @Before
    public void setUp() throws Exception {
        this.fileSystem = mock(FileSystem.class);
        when(fileSystem.getDataFolder()).thenAnswer(answer -> new File("./src/test/resources/data"));
        when(fileSystem.getConfigFolder()).thenAnswer(answer -> new File("./src/test/resources/data/config"));
    }

    @Test
    public void testEn() throws Exception {
        testLoad(new Locale("en"));
        propertiesDataModel.load();
        String value = propertiesDataModel.get("test_property");
        assertEquals("this is a test", value);

        testWithNoFile();
        testLoad(new Locale("en"));
        propertiesDataModel.load();
        value = propertiesDataModel.get("test_property");
        assertEquals("this is a test", value);

        if (fileSystem.getConfigFolder().exists()) {
            FileUtils.deleteDirectory(fileSystem.getConfigFolder());
            fileSystem.getConfigFolder().mkdir();
        }
    }

    @Test
    public void testDe() throws Exception {
        testLoad(new Locale("de"));
        propertiesDataModel.load();
        String value = propertiesDataModel.get("test_property");
        assertEquals("das ist ein test", value);

        testWithNoFile();
        testLoad(new Locale("de"));
        propertiesDataModel.load();
        value = propertiesDataModel.get("test_property");
        assertEquals("das ist ein test", value);

        if (fileSystem.getConfigFolder().exists()) {
            FileUtils.deleteDirectory(fileSystem.getConfigFolder());
            fileSystem.getConfigFolder().mkdir();
        }
    }

    private void testWithNoFile() throws Exception {
        when(fileSystem.getDataFolder()).thenAnswer(answer -> new File("./src/test/resources/data"));
        when(fileSystem.getConfigFolder()).thenAnswer(answer -> new File("./src/test/resources/data/config-error-test"));
    }

    private void testLoad(Locale locale) throws Exception {
        this.propertiesDataModel = new PropertiesDataModel(locale, fileSystem);
    }
}