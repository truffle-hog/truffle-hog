package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;

import java.util.List;

/**
 * <p>
 *     Command used to add Truffle data to the graph. It updates the INodes and IConnections and creates new ones if
 *     necessary (i.e. when new devices enter the network). After the creation, the new nodes get checked with the
 *     Filter objects and marked accordingly.
 * </p>
 */
public class AddPacketDataCommand implements ITruffleCommand {

    private final INetworkWritingPort writingPort;
    private final List<Filter> filterList;
    private final IPacketData data;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check along with the Truffle.
     * </p>
     * @param writingPort {@link INetworkWritingPort} to add data to
     * @param packet Truffle to get data from
     * @param filters List of filters to check
     */
    public AddPacketDataCommand(INetworkWritingPort writingPort, IPacketData packet, List<Filter> filters) {
        this.writingPort = writingPort;
        filterList = filters;
        this.data = packet;
    }

    @Override
    public void execute() {

        final MacAddress sourceAddress = new MacAddress(data.getAttribute(Long.class, "ether_source"));
        final MacAddress destAddress = new MacAddress(data.getAttribute(Long.class, "ether_dest"));

/*        if (writingPort.exists(sourceAddress)) {

        }*/


        final INode sourceNode = new NetworkNode(sourceAddress);
        final INode destNode = new NetworkNode(destAddress);
        final IConnection connection = new NetworkConnection(sourceNode, destNode);

        writingPort.writeConnection(connection);

    }
}
