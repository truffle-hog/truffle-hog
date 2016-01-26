package edu.kit.ipd.trufflehog.service.truffleprocessor;

/**
 * This Interface provides the default methods for any incoming packet data. Packet data is received by e.g.
 * a TruffleReceiver and put into a class that implements this interface.
 */
public interface IPacketData {

    /**
     *
     * @param attributeType the type of attribute that is supposed to be retrieved, for example Integer.class
     * @param attributeIdentifier the string identifier of the attribute that should be retrieved.
     * @param <T> the type of the attribute that is retrieved.
     * @return the value of the attribute or null if nothing was found under the specified identifier
     */
    <T> T getAttribute(Class<T> attributeType, String attributeIdentifier);
}
