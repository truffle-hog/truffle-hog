/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.LiveNetwork;
import edu.kit.trufflehog.model.network.NetworkIOPort;
import edu.kit.trufflehog.model.network.NetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.node.*;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.StaticRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.graph.GraphCopier;
import javafx.beans.property.IntegerProperty;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class TapeCopyCreator implements ICopyCreator, GraphCopier<INode, IConnection> {

    private Collection<IConnection> lastConnectionsCopied = new ArrayList<>();


    @Override
    public INode createDeepCopy(INode node) {

        final INode copy = new NetworkNode(node.getAddress());

        node.stream().forEach(component -> {
            if (component.isMutable()) {

                copy.addComponent(component.createDeepCopy(this));

            } else {
                copy.addComponent(component);
            }
        });
        return copy;
    }

    @Override
    public IConnection createDeepCopy(IConnection connection) {

        final IConnection copy = new NetworkConnection(connection.getSrc().createDeepCopy(this), connection.getDest().createDeepCopy(this));

        connection.stream().forEach(component -> {
            if (component.isMutable()) {

                copy.addComponent(component.createDeepCopy(this));

            } else {
                copy.addComponent(component);
            }
        });
        return copy;
    }

    @Override
    public IRenderer createDeepCopy(MulticastEdgeRenderer multicastEdgeRendererComponent) {

        //edgeRenderer.setOpacity(multicastEdgeRendererComponent.getOpacity());
        //edgeRenderer.setLastUpdate(multicastEdgeRendererComponent.getLastUpdate());
        return new StaticRenderer(multicastEdgeRendererComponent.getShape(),
                multicastEdgeRendererComponent.getColorPicked(), multicastEdgeRendererComponent.getColorUnpicked(), multicastEdgeRendererComponent.getStroke());
    }

    @Override
    public IRenderer createDeepCopy(BasicEdgeRenderer basicEdgeRendererComponent) {

        return new StaticRenderer(basicEdgeRendererComponent.getShape(),
                basicEdgeRendererComponent.getColorPicked(), basicEdgeRendererComponent.getColorUnpicked(), basicEdgeRendererComponent.getStroke());
    }

    @Override
    public IComponent createDeepCopy(EdgeStatisticsComponent edgeStatisticsComponent) {

        return new EdgeStatisticsComponent(edgeStatisticsComponent.getTraffic());
    }

    @Override
    public IRenderer createDeepCopy(StaticRenderer staticRendererComponent) {
        if (staticRendererComponent == null) throw new NullPointerException("staticRendererComponent must not be null!");
        Color picked = staticRendererComponent.getColorPicked();
        Color unpicked = staticRendererComponent.getColorUnpicked();
        Shape shape = staticRendererComponent.getShape();
        Stroke stroke = staticRendererComponent.getStroke();

        return new StaticRenderer(shape, picked, unpicked, stroke);
    }

    @Override
    public IComponent createDeepCopy(ViewComponent viewComponent) {

        final IRenderer comp = viewComponent.getRenderer();

        final StaticRenderer renderer = new StaticRenderer(comp.getShape(),
                comp.getColorPicked(), comp.getColorUnpicked(), comp.getStroke());

        return new ViewComponent(renderer);

    }

    @Override
    public IRenderer createDeepCopy(NodeRenderer nodeRendererComponent) {
        if (nodeRendererComponent == null) throw new NullPointerException("nodeRendererComponent must not be null!");
        Color picked = nodeRendererComponent.getColorPicked();
        Color unpicked = nodeRendererComponent.getColorUnpicked();
        Shape shape = nodeRendererComponent.getShape();

        return new NodeRenderer(shape, picked, unpicked);
    }

    @Override
    public IComponent createDeepCopy(NodeStatisticsComponent nodeStatisticsComponent) {
        if (nodeStatisticsComponent == null) throw new NullPointerException("nodeStatisticsComponent must not be null!");
        IntegerProperty throughput = nodeStatisticsComponent.getCommunicationCountProperty();

        return new NodeStatisticsComponent(throughput.getValue());
    }

    @Override
    public IComponent createDeepCopy(NodeInfoComponent nodeInfoComponent) {
        final NodeInfoComponent nic = new NodeInfoComponent(nodeInfoComponent.getMacAddress());

        if (nodeInfoComponent.getIPAddress() != null) {
            nic.setIPAddress(nodeInfoComponent.getIPAddress());
        }
        if (nodeInfoComponent.getDeviceName() != null) {
            nic.setDeviceName(nodeInfoComponent.getDeviceName());
        }

        return nic;
    }

    @Override
    public IComponent createDeepCopy(PacketDataLoggingComponent packetDataLoggingComponent) {
        if (packetDataLoggingComponent == null) throw new NullPointerException("packetDataLoggingComponent must not be null!");

        return new PacketDataLoggingComponent(packetDataLoggingComponent.getObservablePackets());
    }

    @Override
    public NetworkCopy createDeepCopy(LiveNetwork liveNetwork) {

        this.lastConnectionsCopied = liveNetwork.getObservableGraph().copyEdges(this);
        final NetworkViewCopy viewCopy = liveNetwork.getViewPort().createDeepCopy(this);

        return new NetworkCopy(lastConnectionsCopied, viewCopy.getLocationMap(),
                viewCopy.getMaxThroughput(), viewCopy.getMaxConnectionSize(),
                viewCopy.getViewTime());
    }

    @Override
    public Collection<IConnection> createDeepCopy(NetworkIOPort networkIOPort) {

        final Collection<IConnection> copiedCollection = new ArrayList<>();

        this.lastConnectionsCopied = copiedCollection;
        return copiedCollection;
    }

    @Override
    public NetworkViewCopy createDeepCopy(NetworkViewPort networkViewPort) {

        final Map<IAddress, Point2D> locationMap = new HashMap<>();

        lastConnectionsCopied.stream().forEach(connection -> {

            final IAddress src = connection.getSrc().getAddress();
            final IAddress dest = connection.getDest().getAddress();

            final Point2D srcLoc = networkViewPort.transform(connection.getSrc());
            final Point2D destLoc = networkViewPort.transform(connection.getDest());

            locationMap.put(src, new Point2D.Double(srcLoc.getX(), srcLoc.getY()));
            locationMap.put(dest, new Point2D.Double(destLoc.getX(), destLoc.getY()));
        });

        return new NetworkViewCopy(locationMap, networkViewPort.getMaxConnectionSize(),
                networkViewPort.getMaxThroughput(), networkViewPort.getViewTime());
    }

    @Override
    public Collection<INode> copyVertices(Collection<INode> vertices) {

        final Collection<INode> copied = new ConcurrentLinkedDeque<>();

        vertices.stream().forEach(v -> copied.add(this.createDeepCopy(v)));

        return copied;
    }

    @Override
    public Collection<IConnection> copyEdges(Collection<IConnection> edges) {

        final Collection<IConnection> copied = new ConcurrentLinkedDeque<>();

        edges.stream().forEach(e -> copied.add(this.createDeepCopy(e)));

        return copied;
    }

    @Override
    public IComponent createDeepCopy(FilterPropertiesComponent filterPropertiesComponent) {
        final FilterPropertiesComponent fpc = new FilterPropertiesComponent();

        fpc.getFilterColors().putAll(filterPropertiesComponent.getFilterColors());

        return fpc;
    }
}
