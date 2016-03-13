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
import edu.kit.trufflehog.model.network.graph.components.node.NodeRenderer;

/**
 * <p>
 *     The IFilter interface defines the basic functionality of a filter. A filter is something that checks whether an
 *     {@link INode} matches certain criteria that are defined in the filter (ex. blacklist/whitelist based on IPs). If
 *     a node matches a filter, the filter updates the node's {@link NodeRenderer} where information is kept
 *     about how that node should be displayed by the view. So for example, if a node happens to be on a blacklist, then
 *     the color of the node could be set to red in the NodeRenderer, and the node will be drawn red on the view
 *     to indicate it is part of the blacklist.
 * </p>
 *
 * @author Mark Giraud, Julian Brendl
 * @version 1.1
 */
public interface IFilter extends Comparable<IFilter> {

    IFilter EMPTY = new EmptyFilter();

    /**
     * <p>
     *     Checks if an {@link INode} is contained in the filter. If so, the method updates the node's
     *     {@link NodeRenderer} correspondingly. This is where information is kept about how the node should be
     *     drawn by the view.
     * </p>
     *
     * @param node The {@link INode} to check for a filter matching.
     */
    void check(final INode node);

    /**
     * <p>
     *     Gets the priority of the filter. The priority is used to determine which filter color should be rendered
     *     for example.
     * </p>
     * @return the priority of the filter.
     */
    int getPriority();

    /**
     * <p>
     *     This method clears the filter of all rules and resets all flags set by the filter.
     *     When deleting a filter this method should always be called to clean up the filter state.
     * </p>
     */
    void clear();

    class EmptyFilter implements IFilter {

        @Override
        public void check(INode node) {
            //nothing
        }

        @Override
        public int getPriority() {
            return Integer.MIN_VALUE;
        }

        @Override
        public void clear() {
            //nothing
        }

        @Override
        public int compareTo(IFilter o) {
            return -1;
        }
    }
}