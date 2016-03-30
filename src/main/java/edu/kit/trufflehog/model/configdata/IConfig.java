package edu.kit.trufflehog.model.configdata;

import javafx.beans.property.StringProperty;

import java.util.Properties;

/**
 * <p>
 *     An IConfig is the interface through which settings can be retrieved without having full access to the model.
 *     Since most of TruffleHog's configuration is not hard coded but saved in config files instead, this interface is
 *     the gateway to that configuration data.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface IConfig {
    /**
     * <p>
     *     Gets the value mapped to the given key in the currently loaded {@link Properties} object, or null if the key
     *     does not exist.
     * </p>
     *
     * @param typeClass The type of the object. For example, "007" could be of type java.lang.Integer, that way
     *                  one knows that "007" can be parsed into a real integer value of 007.
     * @param key The key of the value to get in the currently loaded {@link Properties} object.
     * @return The value mapped to the key, if it exists, else null.
     */
    StringProperty getSetting(final Class typeClass, final String key);

    /**
     * <p>
     *     Gets the property from the currently loaded property file associated with the given key.
     * </p>
     * <p>
     *     The key must be the same key that is used in the property file.
     * </p>
     *
     * @param key The key for which to return a value.
     * @return The value mapped to the key in the currently loaded property file.
     */
    String getProperty(final String key);
}
