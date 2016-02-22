package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.uci.ics.jung.graph.Graph;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkIOPort implements INetworkIOPort {

    private final Graph<INode, IConnection> delegate;

    private final Map<Integer, INode> idNodeMap = new ConcurrentHashMap<>();
    private final Map<Integer, IConnection> idConnectionMap = new ConcurrentHashMap<>();

    public NetworkIOPort(final Graph<INode, IConnection> delegate) {

        this.delegate = delegate;
    }

    @Override
    public void writeConnection(IConnection connection) {

        delegate.addEdge(connection, connection.getSrc(), connection.getDest());
        idConnectionMap.put(connection.hashCode(), connection);
    }

    @Override
    public void writeNode(INode node) {

        delegate.addVertex(node);
    }

    @Override
    public INode getNetworkNodeByAddress(IAddress address) {

        return idNodeMap.get(address.hashCode());
    }

    @Override
    public IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest) {
        return null;
    }
}
