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
import edu.kit.trufflehog.model.network.graph.*;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.*;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.beans.property.IntegerProperty;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class TapeCopyCreator implements ICopyCreator {

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

        final StaticRenderer edgeRenderer = new StaticRenderer(multicastEdgeRendererComponent.getShape(),
                multicastEdgeRendererComponent.getColorPicked(), multicastEdgeRendererComponent.getColorUnpicked(), multicastEdgeRendererComponent.getStroke());

        //edgeRenderer.setOpacity(multicastEdgeRendererComponent.getOpacity());
        //edgeRenderer.setLastUpdate(multicastEdgeRendererComponent.getLastUpdate());
        return edgeRenderer;
    }

    @Override
    public IRenderer createDeepCopy(BasicEdgeRenderer basicEdgeRendererComponent) {

        final StaticRenderer edgeRenderer = new StaticRenderer(basicEdgeRendererComponent.getShape(),
                basicEdgeRendererComponent.getColorPicked(), basicEdgeRendererComponent.getColorUnpicked(), basicEdgeRendererComponent.getStroke());

        return edgeRenderer;
    }

    @Override
    public IComponent createDeepCopy(EdgeStatisticsComponent edgeStatisticsComponent) {

        final IComponent copy = new EdgeStatisticsComponent(edgeStatisticsComponent.getTraffic());

        return copy;
    }

    @Override
    public IRenderer createDeepCopy(StaticRenderer staticRendererComponent) {
        if (staticRendererComponent == null) throw new NullPointerException("staticRendererComponent must not be null!");
        Color picked = staticRendererComponent.getColorPicked();
        Color unpicked = staticRendererComponent.getColorUnpicked();
        Shape shape = staticRendererComponent.getShape();
        Stroke stroke = staticRendererComponent.getStroke();

        final IRenderer copy = new StaticRenderer(shape, picked, unpicked, stroke);

        return copy;
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

        final NodeRenderer copy = new NodeRenderer(shape, picked, unpicked);

        return copy;
    }

    @Override
    public IComponent createDeepCopy(NodeStatisticsComponent nodeStatisticsComponent) {
        if (nodeStatisticsComponent == null) throw new NullPointerException("nodeStatisticsComponent must not be null!");
        IntegerProperty throughput = nodeStatisticsComponent.getThroughputProperty();

        NodeStatisticsComponent copy = new NodeStatisticsComponent(throughput.getValue());

        return copy;
    }

    @Override
    public IComponent createDeepCopy(NodeInfoComponent nodeInfoComponent) {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public IComponent createDeepCopy(PacketDataLoggingComponent packetDataLoggingComponent) {
        if (packetDataLoggingComponent == null) throw new NullPointerException("packetDataLoggingComponent must not be null!");

        PacketDataLoggingComponent copy = new PacketDataLoggingComponent(packetDataLoggingComponent.getObservablePackets());

        return copy;
    }

    @Override
    public NetworkCopy createDeepCopy(LiveNetwork liveNetwork) {

        final NetworkViewCopy viewCopy = liveNetwork.getViewPort().createDeepCopy(this);

        return new NetworkCopy(liveNetwork.getWritingPort().createDeepCopy(this), viewCopy.getLocationMap(),
                viewCopy.getMaxThroughput(), viewCopy.getMaxConnectionSize(),
                viewCopy.getViewTime());
    }

    @Override
    public Collection<IConnection> createDeepCopy(NetworkIOPort networkIOPort) {

        networkIOPort.setCopying(true);

        final Collection<IConnection> copiedCollection = new ArrayList<>();

        networkIOPort.getCopyCache().stream().forEach(connection -> {
            copiedCollection.add(connection.createDeepCopy(this));
        });

        // TODO maybe to this in background

        networkIOPort.setCopying(false);

        while (!networkIOPort.getCopyBuffer().isEmpty()) {

            networkIOPort.getCopyCache().add(networkIOPort.getCopyBuffer().remove());
        }

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
        final NetworkViewCopy viewCopy = new NetworkViewCopy(locationMap, networkViewPort.getMaxConnectionSize(),
                networkViewPort.getMaxThroughput(), networkViewPort.getViewTime());

        return viewCopy;
    }
}
