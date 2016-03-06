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
 * @author Julian Brendl
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