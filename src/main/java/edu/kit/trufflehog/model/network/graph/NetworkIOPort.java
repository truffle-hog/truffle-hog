package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.service.packetdataprocessor.IPacketData;
import edu.uci.ics.jung.graph.Graph;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.commons.collections15.keyvalue.MultiKey;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkIOPort implements INetworkIOPort {

    private final Graph<INode, IConnection> delegate;

    private final Map<IAddress, INode> idNodeMap = new ConcurrentHashMap<>();
    private final Map<MultiKey<IAddress>, IConnection> idConnectionMap = new ConcurrentHashMap<>();

    private final IntegerProperty maxThroughputProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty maxConnectionSizeProperty = new SimpleIntegerProperty(0);

    private final MaximumOfValuesBinding maxTrafficBinding = new MaximumOfValuesBinding();
    private final MaximumOfValuesBinding maxThroughputBinding = new MaximumOfValuesBinding();

    //private final IntegerProperty

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
            existing.update(connection);
            return;
        }

        final EdgeStatisticsComponent edgeStat = connection.getComposition().getComponent(EdgeStatisticsComponent.class);

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
            existing.update(node);

            //TODO Improve this!!!
            PacketDataLoggingComponent component = existing.getComposition().getComponent(PacketDataLoggingComponent.class);
            if (component != null) {
                IPacketData packetData = node.getComposition().getComponent(PacketDataLoggingComponent.class).getObservablePackets().get(0);
                if (packetData != null) component.addPacket(packetData);
            }
            return;
        }

        final NodeStatisticsComponent nodeStat = node.getComposition().getComponent(NodeStatisticsComponent.class);

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


    private static class MaximumOfValuesBinding extends IntegerBinding implements ChangeListener<Number>, ListChangeListener<IntegerProperty> {

        private final ObservableList<IntegerProperty> boundProperties = FXCollections.observableArrayList();

        private int max = 0;

        public MaximumOfValuesBinding() {
            super.bind(boundProperties);
            boundProperties.addListener(this);
        }

        public void bindProperty(IntegerProperty property) {

            property.addListener(this);
            super.bind(property);
            boundProperties.add(property);
        }

        @Override
        protected int computeValue() {
            return max;
        }

        @Override
        public void onChanged(Change<? extends IntegerProperty> c) {

            if (c.next()) {

                c.getAddedSubList().stream().forEach(p -> {

                    //System.out.println(p.get());

                    if (p.get() > max) {
                        max = p.get();
                    }
                });
            }
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            //System.out.println(oldValue.intValue() + " " + newValue.intValue());

            if (newValue.intValue() > max) {
                max = newValue.intValue();
            }
        }
    }

}
