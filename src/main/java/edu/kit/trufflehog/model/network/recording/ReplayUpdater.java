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
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;

import javax.xml.soap.Node;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class ReplayUpdater implements IUpdater {

    @Override
    public boolean update(INode node, INode update) {

        update.stream().filter(IComponent::isMutable).forEach(c -> {
            final IComponent existing = node.getComponent(c.getClass());
            existing.update(c, this);
        });
        // TODO check if really some was changed
        return true;
    }

    @Override
    public boolean update(IConnection oldValue, IConnection newValue) {

        newValue.stream().filter(IComponent::isMutable).forEach(c -> {
            final IComponent existing = oldValue.getComponent(c.getClass());
            existing.update(c, this);
        });
        // TODO check if really some was changed
        return true;
    }

    @Override
    public boolean update(NodeStatisticsComponent nodeStatisticsComponent, IComponent instance) {

        if (!(instance instanceof NodeStatisticsComponent)) {
            return false;
        }
        final NodeStatisticsComponent comp = (NodeStatisticsComponent) instance;

        nodeStatisticsComponent.setThroughputProperty(comp.getThroughput());
        return true;
    }

    @Override
    public boolean update(MulticastEdgeRendererComponent multicastEdgeRendererComponent, IComponent instance) {

        if (!(instance instanceof MulticastEdgeRendererComponent)) {
            return false;
        }
        final MulticastEdgeRendererComponent comp = (MulticastEdgeRendererComponent) instance;

        multicastEdgeRendererComponent.setStroke(comp.getStroke());
        multicastEdgeRendererComponent.setShape(comp.getShape());
        multicastEdgeRendererComponent.setColorUnpicked(comp.getColorUnpicked());
        multicastEdgeRendererComponent.setColorPicked(comp.getColorPicked());
        multicastEdgeRendererComponent.setLastUpdate(comp.getLastUpdate());
        return true;
    }

    @Override
    public boolean update(BasicEdgeRendererComponent basicEdgeRendererComponent, IComponent instance) {

        if (!(instance instanceof BasicEdgeRendererComponent)) {
            return false;
        }
        final BasicEdgeRendererComponent comp = (BasicEdgeRendererComponent) instance;

        basicEdgeRendererComponent.setStroke(comp.getStroke());
        basicEdgeRendererComponent.setShape(comp.getShape());
        basicEdgeRendererComponent.setColorUnpicked(comp.getColorUnpicked());
        basicEdgeRendererComponent.setColorPicked(comp.getColorPicked());
        basicEdgeRendererComponent.setCurrentBrightness(comp.getCurrentBrightness());

        return true;
    }

    @Override
    public boolean update(EdgeStatisticsComponent edgeStatisticsComponent, IComponent instance) {

        if (!(instance instanceof EdgeStatisticsComponent)) {
            return false;
        }
        final EdgeStatisticsComponent comp = (EdgeStatisticsComponent) instance;

        edgeStatisticsComponent.setLastUpdateTimeProperty(comp.getLastUpdateTime());
        edgeStatisticsComponent.setTrafficProperty(comp.getTraffic());
        return true;
    }
}
