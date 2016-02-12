package edu.kit.trufflehog.service.packetdataprocessor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Valentin Kiechle on 12.02.2016.
 *
 * Testclass to dummy some Truffles with public methods
 */
public class IPacketDataTestClassDummy implements IPacketData {

    private Map<Class<?>, HashMap<String, Object>> attributes = new HashMap<Class<?>, HashMap<String, Object>>();

    public IPacketDataTestClassDummy() {
        attributes.put(Integer.class, new HashMap<>());
        attributes.put(String.class, new HashMap<>());
        attributes.put(Long.class, new HashMap<>());
    }

    public <T> void setAttribute(Class<T> attributeType, String attributeIdentifier, T value) {
        HashMap<String, Object> attributeMap = attributes.get(attributeType);

        if (attributeMap == null) {
            attributeMap = new HashMap<>();
            attributes.put(attributeType, attributeMap);
        }

        attributeMap.put(attributeIdentifier, value);

    }
    @Override
    public <T> T getAttribute(Class<T> attributeType, String attributeIdentifier) {
        HashMap<?, ?> attributeMap = attributes.get(attributeType);

        if (attributeMap != null)
            return (T) attributeMap.get(attributeIdentifier);

        return null;
    }
}