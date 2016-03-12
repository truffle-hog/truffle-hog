package edu.kit.trufflehog.model.network;

/**
 * <p>
 *     This exception is thrown when an invalid integer representation is passed to a {@link MacAddress}.
 * </p>
 * @author Mark Giraud
 * @version 1.0
 */
public class InvalidMACAddress extends Exception {

    public InvalidMACAddress() {
        super();
    }

    public InvalidMACAddress(String msg) {
        super(msg);
    }
}
