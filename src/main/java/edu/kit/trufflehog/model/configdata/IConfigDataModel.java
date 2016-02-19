package edu.kit.trufflehog.model.configdata;

import javafx.beans.property.StringProperty;

import java.util.Properties;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface IConfigDataModel {
    /**
     * <p>
     *     Gets the value mapped to the given key in the currently loaded {@link Properties} object, or null if the key
     *     does not exist.
     * </p>
     *
     * @param key The key of the value to get in the currently loaded {@link Properties} object.
     * @return The value mapped to the key, if it exists, else null.
     */
    StringProperty getSetting(String key, Class clazz);

    String getFilter(String key);
}
