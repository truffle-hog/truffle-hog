package edu.kit.trufflehog.model.graphlog;

import edu.kit.trufflehog.model.graph.NetworkNode;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * <p>
 *     The TruffleLogger saves {@link Truffle}s to the hard drive. Each {@link NetworkNode} has its own TruffleLogger
 *     instance that it uses to save a Truffle when it receives a new one. This way received packet data can be shown
 *     by the view.
 * </p>
 */
public class TruffleLogger {

    /**
     * <p>
     *     Creates a new TruffleLogger object.
     * </p>
     */
    public TruffleLogger() {
    }

    /**
     * <p>
     *     Returns a list of all Truffles that the {@link NetworkNode} received and that were saved.
     * </p>
     */
    public List<Truffle> getLog() {
        throw new NotImplementedException();
    }

    /**
     * <p>
     *     Saves a Truffle to the hard drive.
     * </p>
     *
     * @param truffle The truffle to save to the hard drive.
     */
    public void log(Truffle truffle) {
    }
}
