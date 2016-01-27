package edu.kit.trufflehog.service.packetdataprocessor;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleReceiver;

/**
 * <p>
 *     This Interface provides the default methods for any incoming packet data which is received by e.g.
 *     a {@link TruffleReceiver}
 *     and put into a class (e.g a {@link Truffle}) that implements this interface.
 * </p>
 *
 * @author Mr. X
 * @version 0.0
 */
public interface IPacketData {

    /**
     * <p>
     *     This method gets an attribute of the packet data object using the specified attribute name.
     * </p>
     * <p>
     *     The function should always return a valid element of the specified type. Each implementation has to make
     *     sure this rule is never violated. In case no element was found under the specified identifier, null is
     *     returned.
     * </p>
     *
     * @param attributeType The type of attribute that is supposed to be retrieved, for example Integer.class
     * @param attributeIdentifier The string identifier of the attribute that should be retrieved.
     * @param <T> The type of the attribute that is retrieved.
     * @return The value of the attribute or null if nothing was found under the specified identifier
     */
    <T> T getAttribute(Class<T> attributeType, String attributeIdentifier);
}
