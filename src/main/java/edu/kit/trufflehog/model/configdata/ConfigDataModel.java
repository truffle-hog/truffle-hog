package edu.kit.trufflehog.model.configdata;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 */
public abstract class ConfigDataModel {

    /**
     *
     */
    public ConfigDataModel() {
    }

    /**
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        throw new NotImplementedException();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setPropertiesFile(String key, String value) {
    }

    /**
     *
     * @param key
     * @param value
     */
    public void loadPropertyFile(String key, String value) {
    }
}
