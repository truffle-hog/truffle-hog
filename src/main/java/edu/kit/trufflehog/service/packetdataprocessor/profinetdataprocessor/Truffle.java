package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is used to store packet data which is received from the spp_profinet snort plugin using
 * the {@link TruffleReceiver}.
 * </p>
 * @author Mr. X
 * @version 0.0
 */
public class Truffle implements IPacketData {

    private Map<Class<?>, HashMap<String, Object>> attributes;

    private Truffle() {
        attributes.put(Integer.class, new HashMap<>());
        attributes.put(String.class, new HashMap<>());
        attributes.put(Long.class, new HashMap<>());
    }

    private <T> void setAttribute(Class<T> attributeType, String attributeIdentifier, T value) {
        HashMap<String, Object> attributeMap = attributes.get(attributeType);

        if (attributeMap == null) {
            attributeMap = new HashMap<>();
            attributes.put(attributeType, attributeMap);
        }

        attributeMap.put(attributeIdentifier, value);

    }

    /**
     * {@inheritDoc}
     *
     * @param attributeType The type of attribute that is supposed to be retrieved, for example Integer.class
     * @param attributeIdentifier The string identifier of the attribute that should be retrieved.
     * @param <T> The type of the attribute that is retrieved.
     * @return The value of the attribute or null if nothing was found under the specified identifier
     */
    @Override
    public <T> T getAttribute(Class<T> attributeType, String attributeIdentifier) {
        HashMap<?, ?> attributeMap = attributes.get(attributeType);

        if (attributeMap != null)
            return (T) attributeMap.get(attributeIdentifier);

        return null;
    }
}
