package edu.kit.trufflehog.model.configdata;

import java.util.Properties;

/**
 * <p>
 *     The IConfigDataModel is an abstraction to any "settings fetch service". In other words, any class that
 *     loads settings from the hard drive should implement this class. Optionally you can pass a specific class to
 *     the get method to clarify of what type you want your result to be, so that you can then safely convert to that
 *     type.
 * </p>
 * <p>
 *     When implementing this class and there is no need for the class type, still implement the
 *     {@link IConfigDataModel#get(Class classType, String key)} method and ignore the additional parameter. Others
 *     can call on the {@link IConfigDataModel#get(String key)} method and your method will still be executed.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface IConfigDataModel<T> {

    /**
     * <p>
     *     Gets the value mapped to the given key in the currently loaded configuration, or null if the key
     *     does not exist. The classType parameter ensures that the returned object has a type of that class. That way
     *     it is safe to convert the object to that type if need be.
     * </p>
     *
     * @param classType The type of the object. For example, "007" could be of type java.lang.Integer, that way
     *                  one knows that "007" can be parsed into a real integer value of 007.
     * @param key The key of the value to get in the currently loaded {@link Properties} object.
     * @return The value mapped to the key, if it exists, else null.
     */
    T get(Class classType, String key);

    /**
     * <p>
     *     Gets the value mapped to the given key in the currently loaded configuration, or null if the key
     *     does not exist.
     * </p>
     *
     * @param key The key of the value to get in the currently loaded {@link Properties} object.
     * @return The value mapped to the key, if it exists, else null.
     */
    default T get(String key) {
        return get(null, key);
    }

    /**
     * <p>
     *     Loads all configurations found on the hard drive into memory.
     * </p>
     */
    void load();
}
