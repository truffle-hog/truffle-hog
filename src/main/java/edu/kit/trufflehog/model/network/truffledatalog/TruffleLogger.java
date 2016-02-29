package edu.kit.trufflehog.model.network.truffledatalog;

import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     The TruffleLogger saves {@link Truffle}s to the hard drive. Each {@link NetworkNode} has its own TruffleLogger
 *     instance that it uses to save a Truffle when it receives a new one. This way received packet data can be shown
 *     by the view.
 * </p>
 */
public class TruffleLogger {

    private List<Truffle> truffleLog;
    /**
     * <p>
     *     Creates a new TruffleLogger object.
     * </p>
     */
    public TruffleLogger() {
        truffleLog = new LinkedList<Truffle>();
    }

    /**
     * <p>
     *     Returns a list of all Truffles that the {@link NetworkNode} received and that were saved.
     * </p>
     */
    public List<Truffle> getLog() {
        throw new UnsupportedOperationException("Not implemented yet!");
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
