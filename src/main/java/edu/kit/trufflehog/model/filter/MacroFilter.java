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

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *     The MacroFilter is the class that should be used when working with appliedFilters. You can add new appliedFilters
 *     through it or check nodes against existing filters. It contains a list of all active nodes, and when a new node
 *     is created, it is checked against all appliedFilters contained in the MacroFilter.
 * </p>
 * <p>
 *     When a filter is found that matches a certain node, the corresponding {@link NodeRenderer} is updated,
 *     where information about how the node should be displayed is kept. The view then renders the node according to its
 *     NodeRenderer.
 * </p>
 *
 * @author Mark Giraud, Julian Brendl
 * @version 1.0
 */
public class MacroFilter implements IFilter {
    private final Set<IFilter> appliedFilters = new HashSet<>();

    /**
     * <p>
     *     Add a new {@link IFilter} to the MacroFilter
     * </p>
     *
     * @param filter The filter to add to the MacroFilter.
     */
    public void addFilter(final IFilter filter) {
        if (filter == null) { throw new NullPointerException("filter should not be null!"); }

        appliedFilters.add(filter);
    }

    /**
     * <p>
     *     Removes an {@link IFilter} from the MacroFilter
     * </p>
     *
     * @param filter The filter to remove from the MacroFilter.
     */
    public void removeFilter(final IFilter filter) {
        if (filter == null) { throw new NullPointerException("filter should not be null!"); }

        filter.clear();
        appliedFilters.remove(filter);
    }

    @Override
    public void check(final INode node) {
        if (node == null) { throw new NullPointerException("node should not be null!"); }

        appliedFilters.stream().forEach(filter -> filter.check(node));
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void clear() {
        appliedFilters.stream().forEach(IFilter::clear);
        appliedFilters.clear();
    }

    @Override
    public Color getFilterColor() {
        // this class never modifies any colors directly. thus null is sufficient.
        return null;
    }

    @Override
    public String getName() {
        return "Macro filter";
    }

    @Override
    public int compareTo(IFilter o) {
        return 1;
    }
}
