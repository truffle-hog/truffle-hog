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

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRendererComponent;
import edu.kit.trufflehog.util.ICopyCreator;

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
    public IComponent createDeepCopy(MulticastEdgeRendererComponent multicastEdgeRendererComponent) {

        final MulticastEdgeRendererComponent edgeRenderer = new MulticastEdgeRendererComponent();

        edgeRenderer.setColorUnpicked(multicastEdgeRendererComponent.getColorUnpicked());
        edgeRenderer.setColorPicked(multicastEdgeRendererComponent.getColorPicked());
        edgeRenderer.setShape(multicastEdgeRendererComponent.getShape());
        edgeRenderer.setStroke(multicastEdgeRendererComponent.getStroke());

        edgeRenderer.setOpacity(multicastEdgeRendererComponent.getOpacity());
        edgeRenderer.setLastUpdate(multicastEdgeRendererComponent.getLastUpdate());
        return edgeRenderer;
    }

    @Override
    public IComponent createDeepCopy(BasicEdgeRendererComponent basicEdgeRendererComponent) {

        final BasicEdgeRendererComponent rendererComponent = new BasicEdgeRendererComponent();
        rendererComponent.setColorPicked(basicEdgeRendererComponent.getColorPicked());
        rendererComponent.setColorUnpicked(basicEdgeRendererComponent.getColorUnpicked());
        rendererComponent.setShape(basicEdgeRendererComponent.getShape());
        rendererComponent.setStroke(basicEdgeRendererComponent.getStroke());

        return rendererComponent;
    }

    @Override
    public IComponent createDeepCopy(EdgeStatisticsComponent edgeStatisticsComponent) {

        final IComponent copy = new EdgeStatisticsComponent(edgeStatisticsComponent.getTraffic());

        return copy;
    }
}
