package edu.kit.trufflehog.model.configdata;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Properties;

/**
 * <p>
 *     The ConfigDataModel mainly manages the GUI state using java property files. It also manages what the gui
 *     displays (e.g. labels etc.). It can save the GUI state and load it back. To do so, each GUI window is mapped to
 *     its own ConfigDatatModel from which it loads its display settings. Thus it is easy to change the language for
 *     example, since only the underlying property file has to be exchanged.
 * </p>
 * <p>
 *     Multiple property objects can be loaded at once, however one has to be always marked as the current object.
 *     This way it is easy to switch between different configurations (for example switch languages).
 * </p>
 */
public abstract class ConfigDataModel {
    /**
     * <p>
     *     Creates a new ConfigDataModel object.
     * </p>
     */
    public ConfigDataModel() {
    }

    /**
     * <p>
     *     Gets the value mapped to the given key in the currently loaded {@link Properties} object, or null if the key
     *     does not exist.
     * </p>
     *
     * @param key The key of the value to get in the currently loaded {@link Properties} object.
     * @return The value mapped to the key, if it exists, else null.
     */
    public String getProperty(String key) {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *      Adds a new property key value pair to the currently loaded {@link Properties} object.
     * </p>
     *
     * @param key The key to add to the currently loaded Properties object.
     * @param value The value to match with the given key.
     */
    public void setProperty(String key, String value) {
    }

    /**
     * <p>
     *     Sets a {@link Properties} object that has been loaded into memory as the current Properties object. That
     *     means that {@link #setProperty(String, String)} and {@link #getProperty(String)} now apply to that property
     *     file.
     * </p>
     *
     * @param name The name of the property file to set as the current property file.
     */
    public void setPropertiesFile(String name) {
    }

    /**
     * <p>
     *      Loads a new property file into memory. It is put on the list of loaded property files in the ConfigDataModel
     *      and can now be selected as the current {@link Properties} object.
     * </p>
     *
     * @param path The path to the property file to load into memory.
     */
    public void loadPropertyFile(String path) {
    }
}
