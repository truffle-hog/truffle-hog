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
package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.StaticRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;

import java.time.Instant;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class LiveUpdater implements IUpdater {

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
    public boolean update(NodeStatisticsComponent nodeStatisticsComponent, IComponent instance) {

        nodeStatisticsComponent.incrementThroughput(1);
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
    public boolean update(MulticastEdgeRendererComponent multicastEdgeRendererComponent, IComponent instance) {

        multicastEdgeRendererComponent.setStrokeWidth(5f);
        multicastEdgeRendererComponent.setMultiplier(1.05f);
        multicastEdgeRendererComponent.setOpacity(170);

        multicastEdgeRendererComponent.setLastUpdate(Instant.now().toEpochMilli());
        return true;
    }

    @Override
    public boolean update(BasicEdgeRendererComponent basicEdgeRendererComponent, IComponent instance) {
        if (basicEdgeRendererComponent.getCurrentBrightness() > 0.7) {
            return true;
        }
        // TODO implement more
        basicEdgeRendererComponent.setCurrentBrightness(1);
        return true;
    }

    @Override
    public boolean update(EdgeStatisticsComponent edgeStatisticsComponent, IComponent instance) {
        // TODO maybe change to another value
        edgeStatisticsComponent.setLastUpdateTimeProperty(Instant.now().toEpochMilli());
        edgeStatisticsComponent.incrementTraffic(1);
        return true;
    }

    @Override
    public boolean update(StaticRendererComponent component, IComponent instance) {

        throw new UnsupportedOperationException("this operation should not be performed by the live updater");
    }

    @Override
    public boolean update(ViewComponent viewComponent, IComponent instance) {

        if (!(instance instanceof ViewComponent)) {
            return false;
        }
        final ViewComponent other = (ViewComponent) instance;

        return viewComponent.getRenderer().update(other.getRenderer(), this);

    }
}
