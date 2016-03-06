package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.node.NodeRendererComponent;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     The MacroFilter is the class that should be used when working with filters. You can add new filters
 *     through it or check nodes against existing filters. It contains a list of all active nodes, and when a new node
 *     is created, it is checked against all filters contained in the MacroFilter.
 * </p>
 * <p>
 *     When a filter is found that matches a certain node, the corresponding {@link NodeRendererComponent} is updated,
 *     where information about how the node should be displayed is kept. The view then renders the node according to its
 *     NodeRendererComponent.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class MacroFilter implements IFilter {
    private final List<IFilter> filters = new LinkedList<>();

    /**
     * <p>
     *     Add a new {@link IFilter} to the MacroFilter
     * </p>
     *
     * @param filter The filter to add to the MacroFilter.
     */
    public void addFilter(final IFilter filter) {
        filters.add(filter);
    }

    /**
     * <p>
     *     Removes an {@link IFilter} from the MacroFilter
     * </p>
     *
     * @param filter The filter to remove from the MacroFilter.
     */
    public void removeFilter(final IFilter filter) {
        filters.remove(filter);
    }

    @Override
    public void check(final INode node) {
        filters.stream().forEach(filter -> filter.check(node));
    }
}
