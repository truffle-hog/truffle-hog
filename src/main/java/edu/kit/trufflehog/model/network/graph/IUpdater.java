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
import edu.kit.trufflehog.model.network.graph.components.node.NodeRendererComponent;
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

    boolean update(INode iComponents, INode updateNode);

    boolean update(NodeStatisticsComponent nodeStatisticsComponent, IComponent instance);

    boolean update(NodeRendererComponent nodeRendererComponent, IComponent instance);

    boolean update(PacketDataLoggingComponent packetDataLoggingComponent, IComponent instance);

    boolean update(IConnection oldValue, IConnection newValue);

    boolean update(MulticastEdgeRendererComponent multicastEdgeRendererComponent, IComponent instance);

    boolean update(BasicEdgeRendererComponent basicEdgeRendererComponent, IComponent instance);

    boolean update(EdgeStatisticsComponent edgeStatisticsComponent, IComponent instance);

    boolean update(StaticRendererComponent component, IComponent instance);

    boolean update(ViewComponent viewComponent, IComponent instance);
}
