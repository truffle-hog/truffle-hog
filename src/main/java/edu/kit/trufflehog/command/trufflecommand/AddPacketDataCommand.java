package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

import java.time.Instant;
import java.util.List;

/**
 * <p>
 *     Command used to add Truffle data to the graph. It updates the INodes and IConnections and creates new ones if
 *     necessary (i.e. when new devices enter the network). After the creation, the new nodes get checked with the
 *     Filter objects and marked accordingly.
 * </p>
 */
public class AddPacketDataCommand implements ITruffleCommand{
    private INetworkGraph networkGraph = null;
    private List<Filter> filterList = null;
    private Truffle truffle = null;
    private Instant createdAtInstant;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check along with the Truffle.
     * </p>
     * @param graph {@link INetworkGraph} to add data to
     * @param packet Truffle to get data from
     * @param filters List of filters to check
     * // TODO: change visibility back to package local once the factory has been created. Without this as public it is hard to test
     */
    public AddPacketDataCommand(INetworkGraph graph,  Truffle packet, List<Filter> filters) {
        networkGraph = graph;
        filterList = filters;
        truffle = packet;
        createdAtInstant = Instant.now();
    }

    public void execute() {

    }

    @Override
    public Instant createdAt() {
        return createdAtInstant;
    }
}
