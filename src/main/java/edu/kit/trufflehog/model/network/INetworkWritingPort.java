package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.util.DeepCopyable;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;

import java.util.Collection;

/**
 * This interface has the sole functionality for writing new network data incoming.
 */
public interface INetworkWritingPort extends DeepCopyable<Collection<IConnection>> {

    /**
     * Writes the given connection into the network.
     * @param connection the connection to be written into the network
     */
    void writeConnection(IConnection connection);

    /**
     * Writes the given Node into the network.
     * @param node the node to be written into teh network
     */
    void writeNode(INode node);

    /**
     * Uses a filter to update all nodes for legality
     * @param filter filter to be applied to the graph
     */
    void applyFilter(IFilter filter);

    ObservableUpdatableGraph<INode, IConnection> getGraph();
}
