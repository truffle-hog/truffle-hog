package edu.kit.trufflehog.command.trufflecommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.AbstractNetworkGraph;
import edu.kit.trufflehog.model.graph.NetworkNode;
import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.Truffle;

import java.util.List;

/**
 * <p>
 *     Command used to add Truffle data to the graph. It updates the INodes and IConnections and creates new ones if
 *     necessary (i.e. when new devices enter the network). After the creation, the new nodes get checked with the
 *     Filter objects and marked accordingly.
 * </p>
 */
public class AddPacketDataCommand implements ITruffleCommand{
    private AbstractNetworkGraph networkGraph = null;
    private List<Filter> filterList = null;
    private Truffle truffle = null;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check along with the Truffle.
     * </p>
     * @param graph {@link AbstractNetworkGraph} to add data to
     * @param packet Truffle to get data from
     * @param filters List of filters to check
     */
    AddPacketDataCommand(AbstractNetworkGraph graph,  Truffle packet, List<Filter> filters) {
        networkGraph = graph;
        filterList = filters;
        truffle = packet;
    }

    public void execute() {
        String sourceMacAdress = truffle.getAttribute(new String().getClass(), "sourceMacAdress");
        String destinationMacAdress = truffle.getAttribute(new String().getClass(), "destinationMacAdress");
        Boolean allowed = true;

        NetworkNode givenSourceNode = networkGraph.getNetworkNodeByMACAddress(sourceMacAdress);
        NetworkNode givenDestinationNode = networkGraph.getNetworkNodeByMACAddress(destinationMacAdress);

        if (givenSourceNode != null && givenDestinationNode != null) {
            networkGraph.addNetworkEdge(givenSourceNode, givenDestinationNode);
        } else {
            for (Filter filter : filterList) {
                if (filter.contains(sourceMacAdress) | filter.contains(destinationMacAdress)) {
                    allowed = false;
                }
            }

            NetworkNode sourceNode = new NetworkNode();
            NetworkNode destinationNode = new NetworkNode();
            sourceNode.setMacAdress(sourceMacAdress);
            destinationNode.setMacAdress(destinationMacAdress);

            networkGraph.addNetworkNode(sourceNode);
            networkGraph.addNetworkNode(destinationNode);
            networkGraph.addNetworkEdge(sourceNode, destinationNode);
        }
    }
}
