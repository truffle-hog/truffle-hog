package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * Created by root on 18.02.16.
 */
public class SnortPNPluginDisconnectFailedException extends Exception {

    SnortPNPluginDisconnectFailedException() {
        super();
    }

    SnortPNPluginDisconnectFailedException(String message) {
        super(message);
    }
}
