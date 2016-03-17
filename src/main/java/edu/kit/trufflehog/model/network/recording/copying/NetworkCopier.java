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
package edu.kit.trufflehog.model.network.recording.copying;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.LiveNetwork;
import edu.kit.trufflehog.model.network.NetworkIOPort;
import edu.kit.trufflehog.model.network.NetworkViewPort;
import edu.kit.trufflehog.model.network.NetworkVisitor;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.StaticRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.model.network.recording.NetworkCopy;
import edu.kit.trufflehog.model.network.recording.NetworkViewCopy;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.graph.GraphCopier;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * \brief
 * \details
 * \date 16.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class NetworkCopier implements NetworkVisitor<NetworkCopy>, ICopyCreator {

    private Collection<IConnection> lastConnectionsCopied = new ArrayList<>();

    private final GraphCopier<INode, IConnection> graphCopier;

    public NetworkCopier(GraphCopier<INode, IConnection> graphCopier) {

        this.graphCopier = graphCopier;
    }

    @Override
    public NetworkCopy visit(LiveNetwork liveNetwork) {

        this.lastConnectionsCopied = liveNetwork.getObservableGraph().copyEdges(graphCopier);
        final NetworkViewCopy viewCopy = liveNetwork.getViewPort().createDeepCopy(this);

        return new NetworkCopy(lastConnectionsCopied, viewCopy.getLocationMap(),
                viewCopy.getMaxThroughput(), viewCopy.getMaxConnectionSize(),
                viewCopy.getViewTime());
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
    public INode createDeepCopy(INode component) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IConnection createDeepCopy(IConnection iComponents) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IRenderer createDeepCopy(MulticastEdgeRenderer multicastEdgeRenderer) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IRenderer createDeepCopy(BasicEdgeRenderer basicEdgeRenderer) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IRenderer createDeepCopy(StaticRenderer staticRenderer) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IRenderer createDeepCopy(NodeRenderer nodeRenderer) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent createDeepCopy(EdgeStatisticsComponent edgeStatisticsComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent createDeepCopy(ViewComponent viewComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent createDeepCopy(NodeStatisticsComponent nodeStatisticsComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent createDeepCopy(NodeInfoComponent nodeInfoComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent createDeepCopy(PacketDataLoggingComponent packetDataLoggingComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public NetworkCopy createDeepCopy(LiveNetwork liveNetwork) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Collection<IConnection> createDeepCopy(NetworkIOPort networkIOPort) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent createDeepCopy(FilterPropertiesComponent filterPropertiesComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }
}
