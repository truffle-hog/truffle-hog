package edu.kit.trufflehog.model.graph;

import org.apache.commons.collections15.Transformer;

/**
 * <p>
 *     Interface to interchange factories creating {@link INetworkGraphLayout} implementations.
 * </p>
 */
public interface ILayoutFactory extends Transformer<AbstractNetworkGraph, INetworkGraphLayout> {
}
