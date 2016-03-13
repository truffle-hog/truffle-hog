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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * <p>
 *     The PropertiesDataModel contains language relevant data like labels or error messages. It loads the properties
 *     file with the correct language at start-up. If the requested language is not found, the default version (english)
 *     is loaded instead.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class PropertiesDataModel extends ConfigDataModel<String> {
    private static final Logger logger = LogManager.getLogger();
    private final FileSystem fileSystem;
    private final Locale locale;
    private Properties properties;

    /**
     * <p>
     *     Creates a new PropertiesDataModel, which gives access to language relevant data. If the requested language is
     *     not found, the default language (english) is loaded instead.
     * </p>
     *
     * @param language The language to load to the configuration data in.
     * @param fileSystem The {@link FileSystem} object that gives access to relevant folders on the hard-drive.
     */
    public PropertiesDataModel(Locale language, FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.locale = language;
        properties = new Properties();

        load();
    }

    /**
     * <p>
     *     Loads all configurations found on the hard drive into memory.
     * </p>
     */
    private void load() {
        // Look for correct language file
        final String fileName = "system_properties_" + locale + ".properties";
        final String fileDefaultName = "system_properties_en.properties";
        File propertyFile;
        try {
            propertyFile = new File(fileSystem.getConfigFolder().getCanonicalPath() + File.separator + fileName);
        } catch (IOException e) {
            logger.error("Unable to calculate canonical path of file");
            return;
        }

        // If not found, load it from resource folder, and if that fails, load the default file in english
        if (!propertyFile.exists() && !copyFromResources(fileName, propertyFile)) {
            try {
                propertyFile = new File(fileSystem.getConfigFolder().getCanonicalPath() + File.separator
                        + fileDefaultName);
            } catch (IOException e) {
                logger.error("Unable to calculate canonical path of file");
                return;
            }
        }

        // Finally set the properties object from the file
        try {
            final InputStream inputStream = new FileInputStream(propertyFile);
            properties.load(inputStream);
        } catch (IOException e) {
            logger.fatal("Unable to find default property file to load");
        }
    }

    @Override
    public String get(Class classType, String key) {
        return properties.getProperty(key);
    }
}
