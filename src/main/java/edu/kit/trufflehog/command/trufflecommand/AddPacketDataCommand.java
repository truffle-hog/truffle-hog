package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.*;
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
        final IPAddress sourceIP = data.getAttribute(IPAddress.class, "sourceIPAddress");

        final MacAddress destAddress = data.getAttribute(MacAddress.class, "destMacAddress");
        final IPAddress destIP = data.getAttribute(IPAddress.class, "destIPAddress");

        final String deviceName = data.getAttribute(String.class, "deviceName");
        final Boolean isResponse = data.getAttribute(Boolean.class, "isResponse");

        // build the source node info
        NodeInfoComponent sourceNIC = new NodeInfoComponent(sourceAddress);
        if (isResponse != null && isResponse) {
            if (deviceName != null) {
                sourceNIC.setDeviceName(deviceName);
            }
            if (sourceIP != null && !sourceIP.equals(IPAddress.INVALID_ADDRESS)) {
                sourceNIC.setIPAddress(sourceIP);
            }
        }

        // build the destination node info
        NodeInfoComponent destNIC = new NodeInfoComponent(destAddress);



        final PacketDataLoggingComponent connectionPacketLogger = new PacketDataLoggingComponent();
        connectionPacketLogger.addPacket(data);

        final PacketDataLoggingComponent srcPacketLogger = new PacketDataLoggingComponent();
        srcPacketLogger.addPacket(data);

        final PacketDataLoggingComponent destPacketLogger = new PacketDataLoggingComponent();
        destPacketLogger.addPacket(data);

        final INode sourceNode = new NetworkNode(sourceAddress, new NodeStatisticsComponent(1, 0), sourceNIC, srcPacketLogger);
        final INode destNode = new NetworkNode(destAddress, new NodeStatisticsComponent(0, 1), destNIC, destPacketLogger);

        final IConnection connection = new NetworkConnection(sourceNode, destNode, new EdgeStatisticsComponent(1), connectionPacketLogger);

        sourceNode.addComponent(new FilterPropertiesComponent());
        destNode.addComponent(new FilterPropertiesComponent());

        sourceNode.addComponent(new ViewComponent(new NodeRenderer()));
        destNode.addComponent(new ViewComponent(new NodeRenderer()));

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
