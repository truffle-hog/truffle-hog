package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.node.MulticastNodeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;


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
    private final List<IFilter> filterList;
    private final IPacketData data;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check along with the Truffle.
     * </p>
     * @param writingPort {@link INetworkWritingPort} to add data to
     * @param packet Truffle to get data from
     * @param filters List of filters to check
     */
    public AddPacketDataCommand(final INetworkWritingPort writingPort, final IPacketData packet, final List<IFilter> filters) {
        this.writingPort = writingPort;
        filterList = filters;
        this.data = packet;
    }

    @Override
    public void execute() {

        final MacAddress sourceAddress = new MacAddress(data.getAttribute(Long.class, "sourceMacAddress"));
        final MacAddress destAddress = new MacAddress(data.getAttribute(Long.class, "destMacAddress"));

        final INode sourceNode = new NetworkNode(sourceAddress);
        final INode destNode = new NetworkNode(destAddress);
        final IConnection connection = new NetworkConnection(sourceNode, destNode);

        destNode.getComposition().addComponent(new PacketDataLoggingComponent());
        destNode.getComposition().getComponent(PacketDataLoggingComponent.class).addPacket(data);
        MulticastNodeRendererComponent mnrc = new MulticastNodeRendererComponent();
        destNode.getComposition().addComponent(mnrc);

        sourceNode.getComposition().addComponent(new PacketDataLoggingComponent());
        sourceNode.getComposition().getComponent(PacketDataLoggingComponent.class).addPacket(data);
        sourceNode.getComposition().addComponent(new MulticastNodeRendererComponent());


        writingPort.writeNode(sourceNode);
        writingPort.writeNode(destNode);
        writingPort.writeConnection(connection);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
