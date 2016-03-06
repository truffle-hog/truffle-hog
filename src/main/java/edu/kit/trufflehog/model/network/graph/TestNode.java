package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import javafx.beans.property.*;

import java.util.Collection;
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
    public IAddress getAddress() {
        return null;
    }

    public String name() {
        return null;
    }

    public boolean isMutable() {
        return false;
    }

    @Override
    public INode createDeepCopy() {
        return null;
    }

    @Override
    public boolean update(INode update) {
        return false;
    }

    @Override
    public IComposition getComposition() {
        return null;
    }

    public boolean update(IComponent update) {
        return false;
    }

    public <T extends IComponent> void addComponent(T component) {

    }

    public void removeComponent(Class<? extends IComponent> type) {

    }

    public <T extends IComponent> T getComponent(Class<T> componentType) {
        return null;
    }

    public Collection<? extends IComponent> getComponents() {
        return null;
    }
}
