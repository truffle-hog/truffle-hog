package edu.kit.trufflehog.model.network;

/**
 * <p>
 *     This exception is thrown if an invalid IP address is constructed.
 * </p>
 * @author Mark Giraud
 */
public class InvalidIPAddress extends Exception {

    public InvalidIPAddress() {
        super();
    }

    public InvalidIPAddress(String msg) {
        super(msg);
    }
}
