package edu.kit.trufflehog.model.graph;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

/**
 * <p>
 *     Interface to interchange factories creating {@link Layout} implementations.
 * </p>
 */
public interface ILayoutFactory extends Transformer<Graph<INode, IConnection>, Layout<INode, IConnection>> {
}
