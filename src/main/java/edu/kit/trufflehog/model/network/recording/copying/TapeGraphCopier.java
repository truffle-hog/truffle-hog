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

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.uci.ics.jung.graph.GraphCopier;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * \brief
 * \details
 * \date 16.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class TapeGraphCopier implements GraphCopier<INode, IConnection> {

    private final NodeCopier nodeCopier;
    private final ConnectionCopier connectionCopier;


    public TapeGraphCopier(NodeCopier nodeCopier, ConnectionCopier connectionCopier) {

        this.nodeCopier = nodeCopier;
        this.connectionCopier = connectionCopier;
    }

    @Override
    public Collection<INode> copyVertices(Collection<INode> vertices) {

        final Collection<INode> copied = new ConcurrentLinkedDeque<>();

        vertices.stream().forEach(v -> copied.add(v.accept(nodeCopier)));

        return copied;
    }

    @Override
    public Collection<IConnection> copyEdges(Collection<IConnection> edges) {

        final Collection<IConnection> copied = new ConcurrentLinkedDeque<>();

        edges.stream().forEach(e -> copied.add(e.accept(connectionCopier)));

        return copied;
    }


}
