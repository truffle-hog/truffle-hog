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

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.uci.ics.jung.graph.GraphCopier;

import java.util.ArrayList;
import java.util.Collection;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class TapeCopyCreator implements IComponentVisitor<IComponent>, GraphCopier<INode, IConnection> {

    private Collection<IConnection> lastConnectionsCopied = new ArrayList<>();

    @Override
    public Collection<INode> copyVertices(Collection<INode> vertices) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Collection<IConnection> copyEdges(Collection<IConnection> edges) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(ViewComponent component) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(EdgeStatisticsComponent edgeStatisticsComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(NodeStatisticsComponent nodeStatisticsComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(NodeInfoComponent nodeInfoComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(FilterPropertiesComponent filterPropertiesComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(PacketDataLoggingComponent packetDataLoggingComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(NetworkNode iComponents) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(NetworkConnection iComponents) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

 /*   @Override
    public Collection<INode> copyVertices(Collection<INode> vertices) {

        final Collection<INode> copied = new ConcurrentLinkedDeque<>();

        vertices.stream().forEach(v -> copied.add(this.visist(v)));

        return copied;
    }

    @Override
    public Collection<IConnection> copyEdges(Collection<IConnection> edges) {

        final Collection<IConnection> copied = new ConcurrentLinkedDeque<>();

        edges.stream().forEach(e -> copied.add(e.accept(this)));

        IComponent c = edges.iterator().next().accept(this);

        return copied;
    }

    @Override
    public INode visit(NetworkNode node) {

        final NetworkNode copy = new NetworkNode(node.getAddress());

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
    public NetworkConnection visit(NetworkConnection connection) {

        final NetworkConnection copy = new NetworkConnection(connection.getSrc().createDeepCopy(this), connection.getDest().createDeepCopy(this));

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
    public IComponent visit(EdgeStatisticsComponent edgeStatisticsComponent) {

        return new EdgeStatisticsComponent(edgeStatisticsComponent.getTraffic());
    }



    @Override
    public IComponent visit(ViewComponent viewComponent) {

        final IRenderer comp = viewComponent.getRenderer();

        final StaticRenderer renderer = new StaticRenderer(comp.getShape(),
                comp.getColorPicked(), comp.getColorUnpicked(), comp.getStroke());

        return new ViewComponent(renderer);

    }



    @Override
    public IComponent visit(NodeStatisticsComponent nodeStatisticsComponent) {
        if (nodeStatisticsComponent == null) throw new NullPointerException("nodeStatisticsComponent must not be null!");
        IntegerProperty throughput = nodeStatisticsComponent.getCommunicationCountProperty();

        return new NodeStatisticsComponent(throughput.getValue());
    }

    @Override
    public IComponent visit(NodeInfoComponent nodeInfoComponent) {
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
    public IComponent visit(PacketDataLoggingComponent packetDataLoggingComponent) {
        if (packetDataLoggingComponent == null) throw new NullPointerException("packetDataLoggingComponent must not be null!");

        return new PacketDataLoggingComponent(packetDataLoggingComponent.getObservablePackets());
    }



    @Override
    public IComponent visit(FilterPropertiesComponent filterPropertiesComponent) {
        final FilterPropertiesComponent fpc = new FilterPropertiesComponent();

        fpc.getFilterColors().putAll(filterPropertiesComponent.getFilterColors());

        return fpc;
    }


    public IRenderer visit(StaticRenderer staticRendererComponent) {
        if (staticRendererComponent == null) throw new NullPointerException("staticRendererComponent must not be null!");
        Color picked = staticRendererComponent.getColorPicked();
        Color unpicked = staticRendererComponent.getColorUnpicked();
        Shape shape = staticRendererComponent.getShape();
        Stroke stroke = staticRendererComponent.getStroke();

        return new StaticRenderer(shape, picked, unpicked, stroke);
    }


    public IRenderer visit(NodeRenderer nodeRendererComponent) {
        if (nodeRendererComponent == null) throw new NullPointerException("nodeRendererComponent must not be null!");
        Color picked = nodeRendererComponent.getColorPicked();
        Color unpicked = nodeRendererComponent.getColorUnpicked();
        Shape shape = nodeRendererComponent.getShape();

        return new NodeRenderer(shape, picked, unpicked);
    }


    public IRenderer visist(MulticastEdgeRenderer multicastEdgeRendererComponent) {

        //edgeRenderer.setOpacity(multicastEdgeRendererComponent.getOpacity());
        //edgeRenderer.setLastUpdate(multicastEdgeRendererComponent.getLastUpdate());
        return new StaticRenderer(multicastEdgeRendererComponent.getShape(),
                multicastEdgeRendererComponent.getColorPicked(), multicastEdgeRendererComponent.getColorUnpicked(), multicastEdgeRendererComponent.getStroke());
    }


    public IRenderer createDeepCopy(BasicEdgeRenderer basicEdgeRendererComponent) {

        return new StaticRenderer(basicEdgeRendererComponent.getShape(),
                basicEdgeRendererComponent.getColorPicked(), basicEdgeRendererComponent.getColorUnpicked(), basicEdgeRendererComponent.getStroke());
    }


    public NetworkCopy visit(LiveNetwork liveNetwork) {

        this.lastConnectionsCopied = liveNetwork.getObservableGraph().copyEdges(this);
        final NetworkViewCopy viewCopy = liveNetwork.getViewPort().createDeepCopy(this);

        return new NetworkCopy(lastConnectionsCopied, viewCopy.getLocationMap(),
                viewCopy.getMaxThroughput(), viewCopy.getMaxConnectionSize(),
                viewCopy.getViewTime());
    }


    public Collection<IConnection> visit(NetworkIOPort networkIOPort) {

        final Collection<IConnection> copiedCollection = new ArrayList<>();

        this.lastConnectionsCopied = copiedCollection;
        return copiedCollection;
    }

    public NetworkViewCopy visit(NetworkViewPort networkViewPort) {

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


*/
}
