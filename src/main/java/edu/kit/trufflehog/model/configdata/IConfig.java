package edu.kit.trufflehog.model.configdata;

import javafx.beans.property.StringProperty;

import java.util.Properties;

/**
 * <p>
 *     Through the IConfig interface it is possible to get any configuration that is required by TruffleHog
 *     at the start. These configurations could be settings from the settings menu, previously configured filters
 *     etc.
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
     * @param key The key of the value to get in the currently loaded {@link Properties} object.
     * @return The value mapped to the key, if it exists, else null.
     */
    StringProperty getSetting(Class classType, String key);

    /**
     * <p>
     *     Gets the filter object that is mapped to the given key.
     * </p>
     *
     * @param key The key that belongs to the filter that should be retrieved.
     * @return The filter object that is mapped to the given key.
     */
    String getFilter(String key);
}
