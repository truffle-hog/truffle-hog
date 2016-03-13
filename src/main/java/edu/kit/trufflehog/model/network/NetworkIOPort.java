package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.kit.trufflehog.util.bindings.MaximumOfValuesBinding;
import edu.uci.ics.jung.graph.Graph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkIOPort implements INetworkIOPort {

    private static final Logger logger = LogManager.getLogger();

    private final Graph<INode, IConnection> delegate;

    private final Map<IAddress, INode> idNodeMap = new ConcurrentHashMap<>();
    private final Map<MultiKey<IAddress>, IConnection> idConnectionMap = new ConcurrentHashMap<>();

    private final IntegerProperty maxThroughputProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxConnectionSizeProperty = new SimpleIntegerProperty(0);

    private final MaximumOfValuesBinding maxTrafficBinding = new MaximumOfValuesBinding();
    private final MaximumOfValuesBinding maxThroughputBinding = new MaximumOfValuesBinding();

    public NetworkIOPort(final Graph<INode, IConnection> delegate) {

        maxConnectionSizeProperty.bind(maxTrafficBinding);
        maxThroughputProperty.bind(maxThroughputBinding);

        this.delegate = delegate;
    }

    @Override
    public void writeConnection(IConnection connection) {

        final MultiKey<IAddress> connectionKey = new MultiKey<>(connection.getSrc().getAddress(), connection.getDest().getAddress());

        if (delegate.addEdge(connection, connection.getSrc(), connection.getDest())) {

            final EdgeStatisticsComponent edgeStat = connection.getComponent(EdgeStatisticsComponent.class);
            if (edgeStat != null) {
                maxTrafficBinding.bindProperty(edgeStat.getTrafficProperty());
            }
            idConnectionMap.put(connectionKey, connection);
        }
    }

    @Override
    public void writeNode(INode node) {

        if (delegate.addVertex(node)) {

            final NodeStatisticsComponent nodeStat = node.getComponent(NodeStatisticsComponent.class);
            if (nodeStat != null) {
                maxThroughputBinding.bindProperty(nodeStat.getThroughputProperty());
            }
            idNodeMap.put(node.getAddress(), node);
        }
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

    @Override
    public Collection<IConnection> createDeepCopy(ICopyCreator copyCreator) {

        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean isMutable() {
        return false;
    }

}
