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
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.StaticRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import javafx.beans.property.IntegerProperty;

/**
 * \brief
 * \details
 * \date 16.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class ComponentCopier implements IComponentVisitor<IComponent> {



    @Override
    public IComponent visit(NetworkNode iComponents) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IComponent visit(NetworkConnection iComponents) {
        throw new UnsupportedOperationException("Operation not implemented yet");
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

        IntegerProperty ingoing = nodeStatisticsComponent.ingoingCountProperty();
        IntegerProperty outgoing = nodeStatisticsComponent.outgoingCountProperty();

        return new NodeStatisticsComponent(outgoing.get(), ingoing.get());
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
}
