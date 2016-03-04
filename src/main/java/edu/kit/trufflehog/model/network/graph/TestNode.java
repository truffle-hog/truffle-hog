package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;
import javafx.beans.property.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hoehler on 29.02.2016.
 */
public class TestNode implements INode {
    final private Map<Class<? extends Property>, HashMap<String, Property>> properties = new HashMap<>();
    final private Map<Class<?>, HashMap<String, Object>> attributes = new HashMap<>();
    final private Map<Class<?>, Class<? extends Property>> apMap = new HashMap<>();

    TestNode() {
        HashMap<String, Property> longMap = new HashMap<>();
        HashMap<String, Property> stringMap = new HashMap<>();
        HashMap<String, Property> intMap = new HashMap<>();

        properties.put(LongProperty.class, longMap);
        properties.put(StringProperty.class, stringMap);
        properties.put(IntegerProperty.class, intMap);

        intMap.put("ip", new SimpleIntegerProperty(123456789));

        apMap.put(Integer.class, IntegerProperty.class);
        apMap.put(String.class, StringProperty.class);
        apMap.put(Long.class, LongProperty.class);
    }

    public Property getProperty(Class<? extends Property> propertyClass, String identifier) {
        HashMap map = properties.get(propertyClass);
        if (map == null) return null;
        Property p = (Property)map.get(identifier);
        return p;
    }

    public <T> T getAttribute(Class<T> attributeClass, String identifier) {
        Class pClass = apMap.get(attributeClass);
        if (pClass != null) {
            Property property = getProperty(pClass, identifier);
            if (property != null) return (T) property.getValue();
        }
        return null;
    }

    public <T> void setAttribute(Class<T> attributeClass, String identifier, T value) {
        Class pClass = apMap.get(attributeClass);
        if (pClass != null) {
            Property property = getProperty(pClass, identifier);
            if (property != null) {
                property.setValue(value);
            }
        }

    }

    @Override
    public void setIpAddress(int ipAddress) {

    }

    @Override
    public void setMaxAddress(long macAddress) {

    }

    @Override
    public void setDeviceName(String deviceName) {

    }

    @Override
    public void setTimeAdded(int timeAdded) {

    }

    @Override
    public void setLastUpdateTime(int lastUpdateTime) {

    }

    @Override
    public void setPackageCountIn(int packageCountIn) {

    }

    @Override
    public void setPackageCountOut(int packageCountOut) {

    }

    @Override
    public int getIpAddress() {
        return 0;
    }

    @Override
    public long getMacAddress() {
        return 0;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public int getTimeAdded() {
        return 0;
    }

    @Override
    public int getLastUpdateTime() {
        return 0;
    }

    @Override
    public int getPackageCountIn() {
        return 0;
    }

    @Override
    public int getPackageCountOut() {
        return 0;
    }

    @Override
    public void log(Truffle truffle) {

    }
}
