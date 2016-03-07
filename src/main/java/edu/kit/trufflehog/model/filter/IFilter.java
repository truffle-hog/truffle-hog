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

package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.node.NodeRendererComponent;

/**
 * <p>
 *     The IFilter interface defines the basic functionality of a filter. A filter is something that checks whether an
 *     {@link INode} matches certain criteria that are defined in the filter (ex. blacklist/whitelist based on IPs). If
 *     a node matches a filter, the filter updates the node's {@link NodeRendererComponent} where information is kept
 *     about how that node should be displayed by the view. So for example, if a node happens to be on a blacklist, then
 *     the color of the node could be set to red in the NodeRendererComponent, and the node will be drawn red on the view
 *     to indicate it is part of the blacklist.
 * </p>
 *
 * @author Julian Brendl, Mark Giraud
 * @version 1.0
 */
public interface IFilter {
    /**
     * <p>
     *     Checks if an {@link INode} is contained in the filter. If so, the method updates the node's
     *     {@link NodeRendererComponent} correspondingly. This is where information is kept about how the node should be
     *     drawn by the view.
     * </p>
     *
     * @param node The {@link INode} to check for a filter matching.
     */
    void check(final INode node);
}