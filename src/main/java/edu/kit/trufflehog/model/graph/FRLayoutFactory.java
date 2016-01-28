package edu.kit.trufflehog.model.graph;

import org.apache.commons.collections15.Transformer;

/**
 * <p>
 *     Creates a {@link INetworkGraphLayout} implementation of the Fruchterman-Reingold algorithm.
 * </p>
 */
public class FRLayoutFactory implements ILayoutFactory{

    @Override
    public INetworkGraphLayout transform(AbstractNetworkGraph iNetworkGraph) {

        return new FruchtermanReingoldLayout(iNetworkGraph);
    }
}
