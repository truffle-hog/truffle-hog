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

import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.*;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.node.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.stream.Collectors;

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

    private Logger logger = LogManager.getLogger();

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
    public boolean update(NodeRenderer nodeRendererComponent, IRenderer instance) {
        //TODO does one need to update this?
        return true;
    }

    @Override
    public boolean update(PacketDataLoggingComponent packetDataLoggingComponent, IComponent instance) {
        return false;
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
    public boolean update(MulticastEdgeRenderer multicastEdgeRenderer, IRenderer instance) {

        multicastEdgeRenderer.setStrokeWidth(5f);
        multicastEdgeRenderer.setMultiplier(1.05f);
        multicastEdgeRenderer.setOpacity(170);

        multicastEdgeRenderer.setLastUpdate(Instant.now().toEpochMilli());
        return true;
    }

    @Override
    public boolean update(BasicEdgeRenderer basicEdgeRenderer, IRenderer instance) {
        if (basicEdgeRenderer.getCurrentBrightness() > 0.7) {
            return true;
        }

        // TODO implement more
        basicEdgeRenderer.setCurrentBrightness(1.0f);
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
    public boolean update(NodeInfoComponent nodeInfoComponent, IComponent instance) {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public boolean update(StaticRenderer component, IRenderer instance) {

        throw new UnsupportedOperationException("this operation should not be performed by the live updater");
    }

    @Override
    public boolean update(ViewComponent viewComponent, IComponent instance) {

        if (!viewComponent.equals(instance)) {
            return false;
        }
        final ViewComponent other = (ViewComponent) instance;
        return viewComponent.getRenderer().update(other.getRenderer(), this);
    }

    @Override
    public boolean update(FilterPropertiesComponent filterPropertiesComponent, IComponent instance) {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }
}
