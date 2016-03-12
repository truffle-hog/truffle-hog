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
import edu.kit.trufflehog.model.network.graph.components.edge.StaticRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeRenderer;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public interface IUpdater {

    //TODO document interface
    //TODO change interface structure or maybe the whole structure so that we don't have a huge bloated interface
    //TODO move this out of util?

    boolean update(INode iComponents, INode updateNode);

    boolean update(IConnection oldValue, IConnection newValue);

    boolean update(NodeRenderer nodeRenderer, IRenderer instance);

    boolean update(MulticastEdgeRenderer multicastEdgeRenderer, IRenderer instance);

    boolean update(BasicEdgeRenderer basicEdgeRenderer, IRenderer instance);

    boolean update(StaticRenderer component, IRenderer instance);

    boolean update(NodeStatisticsComponent nodeStatisticsComponent, IComponent instance);

    boolean update(PacketDataLoggingComponent packetDataLoggingComponent, IComponent instance);

    boolean update(EdgeStatisticsComponent edgeStatisticsComponent, IComponent instance);

    boolean update(NodeInfoComponent nodeInfoComponent, IComponent instance);

    boolean update(ViewComponent viewComponent, IComponent instance);
}
