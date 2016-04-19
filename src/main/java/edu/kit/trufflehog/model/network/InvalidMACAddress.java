package edu.kit.trufflehog.model.network;

/**
 * <p>
 *     This exception is thrown when an invalid integer representation is passed to a {@link MacAddress}.
 * </p>
 * @author Mark Giraud
 * @version 1.0
 */
public class InvalidMACAddress extends IllegalArgumentException {

    public InvalidMACAddress(long value) {
        super("The provided address value is in wrong format: " + Long.toHexString(value));
    }

}
