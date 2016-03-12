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

import com.sun.org.apache.xalan.internal.lib.NodeInfo;
import edu.kit.trufflehog.model.network.LiveNetwork;
import edu.kit.trufflehog.model.network.NetworkIOPort;
import edu.kit.trufflehog.model.network.NetworkViewPort;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.*;
import edu.kit.trufflehog.model.network.graph.components.node.*;
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

    //TODO document interface
    //TODO change interface structure or maybe the whole structure so that we don't have a huge bloated interface
    //TODO move this out of util?

    INode createDeepCopy(INode component);

    IConnection createDeepCopy(IConnection iComponents);

    IRenderer createDeepCopy(MulticastEdgeRenderer multicastEdgeRenderer);

    IRenderer createDeepCopy(BasicEdgeRenderer basicEdgeRenderer);

    IRenderer createDeepCopy(StaticRenderer staticRenderer);

    IRenderer createDeepCopy(NodeRenderer nodeRenderer);

    IComponent createDeepCopy(EdgeStatisticsComponent edgeStatisticsComponent);

    IComponent createDeepCopy(ViewComponent viewComponent);

    IComponent createDeepCopy(NodeStatisticsComponent nodeStatisticsComponent);

    IComponent createDeepCopy(NodeInfoComponent nodeInfoComponent);

    IComponent createDeepCopy(PacketDataLoggingComponent packetDataLoggingComponent);

    NetworkCopy createDeepCopy(LiveNetwork liveNetwork);

    Collection<IConnection> createDeepCopy(NetworkIOPort networkIOPort);

    NetworkViewCopy createDeepCopy(NetworkViewPort networkViewPort);

    IComponent createDeepCopy(FilterPropertiesComponent filterPropertiesComponent);
}
