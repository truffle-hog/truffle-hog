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
package edu.kit.trufflehog.model.jung.layout;

import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.uci.ics.jung.algorithms.layout.Layout;

/**
 * \brief
 * \details
 * \date 21.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public abstract class LayoutEvent<V extends IComposition, E extends IComposition> {

    private final Layout<V,E> source;
    private final Type type;

    /**
     * Creates an instance with the specified {@code source} graph and {@code Type}
     * (vertex/edge addition/removal).
     */
    public LayoutEvent(Layout<V, E> source, Type type) {
        this.source = source;
        this.type = type;
    }

    /**
     * Types of graph events.
     */
    public enum Type {
        VERTEX_MOVED
    }

}

