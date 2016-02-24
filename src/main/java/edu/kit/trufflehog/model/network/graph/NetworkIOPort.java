package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.keyvalue.MultiKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkIOPort implements INetworkIOPort {

    private final Graph<INode, IConnection> delegate;

    private final Map<IAddress, INode> idNodeMap = new ConcurrentHashMap<>();
    private final Map<MultiKey<IAddress>, IConnection> idConnectionMap = new ConcurrentHashMap<>();

    public NetworkIOPort(final Graph<INode, IConnection> delegate) {

        this.delegate = delegate;
    }

    @Override
    public void writeConnection(IConnection connection) {

        delegate.addEdge(connection, connection.getSrc(), connection.getDest());

        final MultiKey<IAddress> connectionKey = new MultiKey<>(connection.getSrc().getAddress(), connection.getDest().getAddress());

        idConnectionMap.put(connectionKey, connection);
    }

    @Override
    public void writeNode(INode node) {

        delegate.addVertex(node);
        idNodeMap.put(node.getAddress(), node);

        System.out.println(delegate);
    }

    @Override
    public INode getNetworkNodeByAddress(IAddress address) {

        return idNodeMap.get(address);
    }

    @Override
    public IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest) {
        final MultiKey<IAddress> retriever = new MultiKey<>(source, dest);
        return idConnectionMap.get(retriever);
    }
}
