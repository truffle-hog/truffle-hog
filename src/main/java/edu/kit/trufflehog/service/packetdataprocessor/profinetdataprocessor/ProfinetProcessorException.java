package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * <p>
 *     This exception is the base of all exceptions that may occur in the profinet data processor
 * </p>
 *
 * @author Mark Giraud
 * @version 1.0
 */
public class ProfinetProcessorException extends Exception {

    public ProfinetProcessorException() {
        super();
    }

    public ProfinetProcessorException(String msg) {
        super(msg);
    }
}
