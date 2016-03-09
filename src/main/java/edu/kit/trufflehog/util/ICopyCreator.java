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
package edu.kit.trufflehog.util;

import edu.kit.trufflehog.model.network.LiveNetwork;
import edu.kit.trufflehog.model.network.NetworkIOPort;
import edu.kit.trufflehog.model.network.NetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.ViewComponent;
import edu.kit.trufflehog.model.network.recording.NetworkCopy;
import edu.kit.trufflehog.model.network.recording.NetworkViewCopy;

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
public interface ICopyCreator {

    INode createDeepCopy(INode component);

    IConnection createDeepCopy(IConnection iComponents);

    IComponent createDeepCopy(MulticastEdgeRendererComponent multicastEdgeRendererComponent);

    IComponent createDeepCopy(BasicEdgeRendererComponent basicEdgeRendererComponent);

    IComponent createDeepCopy(EdgeStatisticsComponent edgeStatisticsComponent);

    IComponent createDeepCopy(ViewComponent viewComponent);

    NetworkCopy createDeepCopy(LiveNetwork liveNetwork);

    Collection<IConnection> createDeepCopy(NetworkIOPort networkIOPort);

    NetworkViewCopy createDeepCopy(NetworkViewPort networkViewPort);

}
