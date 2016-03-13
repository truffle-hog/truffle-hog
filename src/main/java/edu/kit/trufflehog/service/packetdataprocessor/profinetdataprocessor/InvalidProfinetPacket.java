package edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor;

/**
 * <p>
 *     This exception is thrown if the received packet was invalid
 * </p>
 *
 * @author Mark Giraud
 * @version 1.0
 */
public class InvalidProfinetPacket extends ProfinetProcessorException {

    public InvalidProfinetPacket() {
        super();
    }

    public InvalidProfinetPacket(String msg) {
        super(msg);
    }
}
