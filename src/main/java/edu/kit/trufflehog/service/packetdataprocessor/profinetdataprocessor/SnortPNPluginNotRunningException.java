package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * <p>
 *     This exception is thrown when a TruffleReceiver could not connect to the spp_profinet snort plugin
 * </p>
 */
public class SnortPNPluginNotRunningException extends ProfinetProcessorException {

    public SnortPNPluginNotRunningException() {
        super();
    }

    public SnortPNPluginNotRunningException(final String message) {
        super(message);
    }
}
