package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.*;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;

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
    private IPacketData truffle = null;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check along with the Truffle.
     * </p>
     * @param graph {@link INetworkGraph} to add data to
     * @param packet Truffle to get data from
     * @param filters List of filters to check
     */

    AddPacketDataCommand(INetworkGraph graph,  IPacketData packet, List<Filter> filters) {

        networkGraph = graph;
        filterList = filters;
        truffle = packet;
    }

    public void execute() {
        Long sourceMacAddress = truffle.getAttribute(Long.class, "sourceMacAddress");
        Long destinationMacAddress = truffle.getAttribute(Long.class, "destinationMacAddress");
        boolean allowedSource = true;
        boolean allowedDestination = true;
        IConnection edge;

        INode sourceNode = networkGraph.getNetworkNodeByMACAddress(sourceMacAddress);
        INode destinationNode = networkGraph.getNetworkNodeByMACAddress(destinationMacAddress);

        if (sourceNode != null && destinationNode != null) {  //Connection is existing
            edge = networkGraph.getNetworkEdge(sourceNode, destinationNode);
        } else {  //Otherwise create a new one
            edge = new NetworkEdge();
            networkGraph.addNetworkEdge(edge, sourceNode, destinationNode);
        }
        if (sourceNode == null) {  //Creating new source node because it doesn´t exist
            sourceNode = new NetworkNode();
            sourceNode.setMacAdress(sourceMacAddress);
            networkGraph.addNetworkNode(sourceNode);
        }
        if (destinationNode == null) {  //Creating new destination node because it doesn´t exist
            destinationNode = new NetworkNode();
            destinationNode.setMacAdress(destinationMacAddress);
            networkGraph.addNetworkNode(destinationNode);
        }
        for (Filter filter : filterList) {
            if (filter.contains(sourceMacAddress.toString())) {
                allowedSource = false;
            }
            if (filter.contains(destinationMacAddress.toString())) {
                allowedDestination = false;
            }
        }
        //TODO call legalisation method for nodes

        sourceNode.setPackageCountOut(sourceNode.getPackageCountOut() + 1);
        sourceNode.setLastUpdateTime(System.currentTimeMillis());

        destinationNode.setPackageCountIn(destinationNode.getPackageCountIn() + 1);
        destinationNode.setLastUpdateTime(System.currentTimeMillis());

        edge.setTotalPacketCount(edge.getTotalPacketCount() + 1);
    }
}
