package edu.kit.trufflehog.model.network;

/**
 * <p>
 *     This exception is thrown if an invalid IP address is constructed.
 * </p>
 * @author Mark Giraud
 */
public class InvalidIPAddress extends IllegalArgumentException {

    public InvalidIPAddress(long value) {
        super("The provided address value is in wrong format: " + value);
    }

}
