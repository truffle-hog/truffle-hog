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
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.util.DeepCopyable;
import edu.kit.trufflehog.util.Updatable;

import java.io.Serializable;

/**
 * \brief
 * \details
 * \date 04.03.16
 * \copyright GNU Public License
 * @author Jan Hermes
 * @version 0.0.1
 */
public interface IComponent extends DeepCopyable<IComponent>, Updatable<IComponent>, Serializable {

    /**
     * @return the name of this component
     */
    String name();

    <T> T accept(IComponentVisitor<T> visitor);
}
