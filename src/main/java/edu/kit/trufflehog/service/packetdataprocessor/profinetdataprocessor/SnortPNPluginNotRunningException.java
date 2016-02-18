package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * <p>
 *     This exception is thrown when a TruffleReceiver could not connect to the spp_profinet snort plugin
 * </p>
 */
public class SnortPNPluginNotRunningException extends Exception {

    public SnortPNPluginNotRunningException(String message) {
        super(message);
    }
}
