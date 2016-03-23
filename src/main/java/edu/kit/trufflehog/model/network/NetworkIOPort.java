package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.kit.trufflehog.util.bindings.MaximumOfValuesBinding;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jan Hermes
 * @version 0.5
 */
public class NetworkIOPort implements INetworkIOPort {

    private static final Logger logger = LogManager.getLogger();

    private final ObservableUpdatableGraph<INode, IConnection> delegate;

    private final Map<IAddress, INode> idNodeMap = new ConcurrentHashMap<>();
    private final Map<MultiKey<IAddress>, IConnection> idConnectionMap = new ConcurrentHashMap<>();

    private final IntegerProperty maxThroughputProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxConnectionSizeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty population = new SimpleIntegerProperty(0);
    private final DoubleProperty throughput = new SimpleDoubleProperty(0);

    private final MaximumOfValuesBinding maxTrafficBinding = new MaximumOfValuesBinding();
    private final MaximumOfValuesBinding maxThroughputBinding = new MaximumOfValuesBinding();

    public NetworkIOPort(final ObservableUpdatableGraph<INode, IConnection> delegate) {

        maxConnectionSizeProperty.bind(maxTrafficBinding);
        maxThroughputProperty.bind(maxThroughputBinding);

        this.delegate = delegate;
    }

    @Override
    synchronized public void writeConnection(IConnection connection) {

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
    synchronized public void writeNode(INode node) {

        if (delegate.addVertex(node)) {

            final NodeStatisticsComponent nodeStat = node.getComponent(NodeStatisticsComponent.class);
            if (nodeStat != null) {
                maxThroughputBinding.bindProperty(nodeStat.getCommunicationCountProperty());
            }
            idNodeMap.put(node.getAddress(), node);
        }
    }

    @Override
    synchronized public void applyFilter(IFilter filter) {
        delegate.getVertices().forEach(filter::check);
    }

    @Override
    public ObservableUpdatableGraph<INode, IConnection> getGraph() {
        return delegate;
    }

    @Override
    synchronized public Collection<INode> getNetworkNodes() {
        return new LinkedList<>(delegate.getVertices());
    }

    @Override
    synchronized public Collection<IConnection> getNetworkConnections() {
        return new LinkedList<>(delegate.getEdges());
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
    public void setPopulation(int value) {
        population.setValue(value);
    }

    @Override
    public int getPopulation() {
        return population.getValue();
    }

    @Override
    public IntegerProperty getPopulationProperty() {
        return population;
    }

    @Override
    public void setThroughput(double value) {
        throughput.setValue(value);
    }

    @Override
    public double getThroughput() {
        return throughput.getValue();
    }

    @Override
    public DoubleProperty getThroughputProperty() {
        return throughput;
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
