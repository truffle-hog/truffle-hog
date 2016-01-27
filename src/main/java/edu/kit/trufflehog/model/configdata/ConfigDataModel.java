package edu.kit.trufflehog.model.configdata;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 */
public abstract class ConfigDataModel {


    public String getProperty(String key) {
        throw new NotImplementedException();
    }

    public void setProperty(String key, String value) {
    }

    public void setPropertiesFile(String key, String value) {
    }

    public void loadPropertyFile(String key, String value) {
    }
}
