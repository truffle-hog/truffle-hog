package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * <p>
 *     This exception can be thrown by {@link TruffleReceiver}s to indicate that something went wrong while
 *     disconnecting from the Snort Plugin.
 * </p>
 * @author Mark Giraud
 * @version 1.0
 */
public class SnortPNPluginDisconnectFailedException extends Exception {

    SnortPNPluginDisconnectFailedException() {
        super();
    }

    SnortPNPluginDisconnectFailedException(final String message) {
        super(message);
    }
}
