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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * <p>
 *     The ConfigDataModel is an abstraction to any "settings fetch service". In other words, any class that
 *     loads settings from the hard drive should extend this class. Optionally you can pass a specific class to
 *     the get method to clarify of what type you want your result to be, so that you can then safely convert to that
 *     type.
 * </p>
 * <p>
 *     When extendin this class and there is no need for the class type, still implement the
 *     {@link ConfigDataModel#get(Class classType, String key)} method and ignore the additional parameter. Others
 *     can call on the {@link ConfigDataModel#get(String key)} method and your method will still be executed.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
abstract class ConfigDataModel<T> {
    private static final Logger logger = LogManager.getLogger();

    /**
     * <p>
     *     Gets the value mapped to the given key in the currently loaded configuration, or null if the key
     *     does not exist. The classType parameter ensures that the returned object has a type of that class. That way
     *     it is safe to convert the object to that type if need be.
     * </p>
     *
     * @param classType The type of the object. For example, "007" could be of type java.lang.Integer, that way
     *                  one knows that "007" can be parsed into a real integer value of 007.
     * @param key The key of the value that should be retrieved.
     * @return The value mapped to the key, if it exists, else null.
     */
    abstract T get(Class classType, String key);

    /**
     * <p>
     *     Gets the value mapped to the given key in the currently loaded configuration, or null if the key
     *     does not exist.
     * </p>
     *
     * @param key The key of the value that should be retrieved.
     * @return The value mapped to the key, if it exists, else null.
     */
    T get(String key) {
        return get(null, key);
    }

    /**
     * <p>
     *     Copies the a file from the resources folder to the target file.
     * </p>
     *
     * @param fileName The name of the file to copy
     * @param targetFile The file object to which to copy the file
     * @return True if the copy operation was successful, else false
     */
    boolean copyFromResources(final String fileName, final File targetFile) {
        // Set file path to the default file in resources
        final String filePath = File.separator + "edu" + File.separator + "kit" + File.separator + "trufflehog" +
                File.separator + "config" + File.separator + fileName;

        // Get the file from the resources
        try (InputStream inputStream = this.getClass().getResourceAsStream((filePath))) {
            try (OutputStream outputStream = new FileOutputStream(targetFile)) {
                int c;
                while ((c = inputStream.read()) != -1) {
                    outputStream.write(c);
                }
            } catch (IOException e) {
                logger.error("Error writing to output stream", e);
                return false;
            }
        } catch (IOException e) {
            logger.error("Error reading from input stream", e);
            return false;
        }
        return true;
    }
}
