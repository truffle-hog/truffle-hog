package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     This class is used to store packet data which is received from the spp_profinet snort plugin using
 *     the {@link TruffleReceiver}.
 * </p>
 *
 * @author Mark Giraud
 * @version 1.0
 */
public class Truffle implements IPacketData {

    private final Map<Class<?>, Map<String, Object>> attributes = new HashMap<>();

    public Truffle(long sourceMacID, long destinationMacID) {

        setAttribute(Long.class, "ether_source", sourceMacID);
        setAttribute(Long.class, "ether_dest", destinationMacID);
    }

    /**
     * <p>
     *     This method adds a new element under the specified type and name to the Truffle.
     * </p>
     * @param attributeType The type of the element to add.
     * @param attributeIdentifier The string identifier of the object.
     * @param value The value to add under the identifier.
     * @param <T> The Type of the element to add.
     * @return The value of the previous mapping. If no element was mapped under the identifier then null is returned.
     */
    @SuppressWarnings("unchecked")
    <T> T setAttribute(Class<T> attributeType, String attributeIdentifier, T value) {

        Map<String, Object> attributeMap = attributes.get(attributeType);

        if (attributeMap == null) {
            attributeMap = new HashMap<>();
            attributes.put(attributeType, attributeMap);
        }

        return (T) attributeMap.put(attributeIdentifier, value);

    }

    /**
     * {@inheritDoc}
     *
     * @param attributeType The type of attribute that is supposed to be retrieved, for example Integer.class
     * @param attributeIdentifier The string identifier of the attribute that should be retrieved.
     * @param <T> The type of the attribute that is retrieved.
     * @return The value of the attribute or null if nothing was found under the specified identifier
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(Class<T> attributeType, String attributeIdentifier) {

        final Map<?, ?> attributeMap = attributes.get(attributeType);

        if (attributeMap == null)
            return null;

        return (T) attributeMap.get(attributeIdentifier);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Long srcMacAddress = getAttribute(Long.class, "sourceMacAddress");
        Long destMacAddress = getAttribute(Long.class, "destMacAddress");
        Short etherType = getAttribute(Short.class, "etherType");

        sb.append("Source mac address: ");
        if (srcMacAddress != null) {
            sb.append(Long.toHexString(srcMacAddress));
        }
        sb.append("\nDestination mac address: ");
        if (destMacAddress != null) {
            sb.append(Long.toHexString(destMacAddress));
        }
        sb.append("\nEther type: ");
        if (etherType != null) {
            sb.append(etherType.toString());
        }
        sb.append("\n\n");

        return sb.toString();
    }
}