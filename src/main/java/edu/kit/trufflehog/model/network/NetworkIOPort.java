package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.LiveUpdater;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.util.bindings.MaximumOfValuesBinding;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.commons.collections15.keyvalue.MultiKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkIOPort implements INetworkIOPort {

    private final Graph<INode, IConnection> delegate;

    private final Map<IAddress, INode> idNodeMap = new ConcurrentHashMap<>();
    private final Map<MultiKey<IAddress>, IConnection> idConnectionMap = new ConcurrentHashMap<>();

    private final Queue<IConnection> copyCache = new LinkedList<>();
    private final Queue<IConnection> whileCopyBuffer = new LinkedList<>();

    private final IntegerProperty maxThroughputProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxConnectionSizeProperty = new SimpleIntegerProperty(0);

    private final MaximumOfValuesBinding maxTrafficBinding = new MaximumOfValuesBinding();
    private final MaximumOfValuesBinding maxThroughputBinding = new MaximumOfValuesBinding();

    private final IUpdater liveUpdater = new LiveUpdater();

    public NetworkIOPort(final Graph<INode, IConnection> delegate) {

        maxConnectionSizeProperty.bind(maxTrafficBinding);
        maxThroughputProperty.bind(maxThroughputBinding);

        this.delegate = delegate;
    }

    @Override
    public void writeConnection(IConnection connection) {

        final MultiKey<IAddress> connectionKey = new MultiKey<>(connection.getSrc().getAddress(), connection.getDest().getAddress());
        final IConnection existing = idConnectionMap.get(connectionKey);



        if (existing != null) {
            existing.update(connection, liveUpdater);
            return;
        }
        final EdgeStatisticsComponent edgeStat = connection.getComponent(EdgeStatisticsComponent.class);

        if (edgeStat != null) {
            maxTrafficBinding.bindProperty(edgeStat.getTrafficProperty());
        }
        delegate.addEdge(connection, connection.getSrc(), connection.getDest());
        idConnectionMap.put(connectionKey, connection);
    }

    @Override
    public void writeNode(INode node) {

        final INode existing = idNodeMap.get(node.getAddress());

        if (existing != null) {
            existing.update(node, liveUpdater);
            return;
        }
        final NodeStatisticsComponent nodeStat = node.getComponent(NodeStatisticsComponent.class);

        if (nodeStat != null) {
            maxThroughputBinding.bindProperty(nodeStat.getThroughputProperty());
        }

        delegate.addVertex(node);
        idNodeMap.put(node.getAddress(), node);
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

    @Override
    public int getMaxConnectionSize() {
        return maxConnectionSizeProperty.get();
    }

    @Override
    public IntegerProperty getMaxConnectionSizeProperty() {
        return maxConnectionSizeProperty;
    }

    @Override
    public int getMaxThroughput() {
        return maxThroughputProperty.get();
    }


    @Override
    public IntegerProperty getMaxThroughputProperty() {
        return maxThroughputProperty;
    }


}
