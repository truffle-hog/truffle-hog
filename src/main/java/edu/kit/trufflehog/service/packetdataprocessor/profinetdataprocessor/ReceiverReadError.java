package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * This exception can be thrown if there was an error reading from the ipc interface. For example if only half
 * of a packet arrived we want to notify the rest of the program that this happened.
 *
 * @author Mark Giraud
 * @version 1.0
 */
public class ReceiverReadError extends Exception {

    public ReceiverReadError() {

    }

    public ReceiverReadError(String message) {
        super(message);
    }
}
