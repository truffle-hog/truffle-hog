package de.fraunhofer.iosb.trufflehog.service.packetdataprocessor;

import de.fraunhofer.iosb.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleReceiver;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to store packet data which is received from the spp_profinet snort plugin using
 * the {@link TruffleReceiver}.
 * @author Mr. X
 * @version 0.0
 */
public class Truffle implements IPacketData {

    private Map<Class<?>, HashMap<String, Object>> attributes;

    public Truffle() {
        attributes.put(Integer.class, new HashMap<>());
        attributes.put(String.class, new HashMap<>());
        attributes.put(Long.class, new HashMap<>());
    }

    private <T> void setAttribute(Class<T> attributeType, String attributeIdentifier, T value) {
        HashMap<String, Object> attributeMap = attributes.get(attributeType);

        if (attributeMap != null)
            attributeMap.put(attributeIdentifier, value);
    }

    @Override
    public <T> T getAttribute(Class<T> attributeType, String attributeIdentifier) {
        return (T) attributes.get(attributeType).get(attributeIdentifier);
    }
}
