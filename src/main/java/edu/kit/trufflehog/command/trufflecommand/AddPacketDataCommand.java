package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;

import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;


/**
 * <p>
 *     Command used to add Truffle data to the graph. It updates the INodes and IConnections and creates new ones if
 *     necessary (i.e. when new devices enter the network). After the creation, the new node get checked with the
 *     Filter objects and marked accordingly.
 * </p>
 */
public class AddPacketDataCommand implements ITruffleCommand {

    private final INetworkWritingPort writingPort;
    private final IFilter filter;
    private final IPacketData data;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check along with the Truffle.
     * </p>
     * @param writingPort {@link INetworkWritingPort} to add data to
     * @param packet Truffle to get data from
     * @param filter The filter to check.
     */

    public AddPacketDataCommand(INetworkWritingPort writingPort, IPacketData packet, IFilter filter) {
        this.writingPort = writingPort;
        this.filter = filter;
        this.data = packet;
    }

    @Override
    public void execute() {

        final MacAddress sourceAddress = data.getAttribute(MacAddress.class, "sourceMacAddress");
        final MacAddress destAddress = data.getAttribute(MacAddress.class, "destMacAddress");

        final PacketDataLoggingComponent connectionPacketLogger = new PacketDataLoggingComponent();
        final PacketDataLoggingComponent srcPacketLogger = new PacketDataLoggingComponent();
        final PacketDataLoggingComponent destPacketLogger = new PacketDataLoggingComponent();
        connectionPacketLogger.addPacket(data);
        srcPacketLogger.addPacket(data);
        destPacketLogger.addPacket(data);

        final INode sourceNode = new NetworkNode(sourceAddress, new NodeStatisticsComponent(1), new NodeInfoComponent(sourceAddress), srcPacketLogger);
        final INode destNode = new NetworkNode(destAddress, new NodeStatisticsComponent(1), new NodeInfoComponent(destAddress), destPacketLogger);





        final IConnection connection = new NetworkConnection(sourceNode, destNode, new EdgeStatisticsComponent(1), connectionPacketLogger);

        sourceNode.addComponent(new ViewComponent(new NodeRenderer()));
        destNode.addComponent(new ViewComponent(new NodeRenderer()));

        sourceNode.addComponent(new FilterPropertiesComponent());
        destNode.addComponent(new FilterPropertiesComponent());

        if (destAddress.isMulticast()) {
            connection.addComponent(new ViewComponent(new MulticastEdgeRenderer()));
        } else {
            connection.addComponent(new ViewComponent(new BasicEdgeRenderer()));
        }

        filter.check(sourceNode);
        filter.check(destNode);

        writingPort.writeNode(sourceNode);
        writingPort.writeNode(destNode);
        writingPort.writeConnection(connection);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
