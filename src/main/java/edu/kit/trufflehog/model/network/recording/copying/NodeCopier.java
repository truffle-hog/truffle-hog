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
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.NetworkNodeTester;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;

/**
 * \brief
 * \details
 * \date 16.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class NodeCopier implements IComponentVisitor<INode> {

    private final IComponentVisitor<IComponent> componentCopier;

    public NodeCopier(IComponentVisitor<IComponent> componentCopier) {
        this.componentCopier = componentCopier;
    }

    @Override
    public INode visit(NetworkNode node) {

        final NetworkNode copy = new NetworkNode(node.getAddress());

        node.stream().forEach(component -> {
            if (component.isMutable()) {

                copy.addComponent(component.accept(componentCopier));

            } else {
                copy.addComponent(component);
            }
        });
        return copy;
    }

    @Override
    public INode visit(NetworkConnection iComponents) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    // TODO Delete
    @Override
    public INode visit(NetworkNodeTester iComponents) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }


    @Override
    public INode visit(ViewComponent component) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public INode visit(EdgeStatisticsComponent edgeStatisticsComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public INode visit(NodeStatisticsComponent nodeStatisticsComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public INode visit(NodeInfoComponent nodeInfoComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public INode visit(FilterPropertiesComponent filterPropertiesComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public INode visit(PacketDataLoggingComponent packetDataLoggingComponent) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }


    public IComponentVisitor<IComponent> getComponentCopier() {
        return componentCopier;
    }
}
