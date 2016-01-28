package edu.kit.trufflehog.model.graph;

import org.apache.commons.collections15.Transformer;

/**
 * <p>
 *     Creates a {@link INetworkGraphLayout} implementation of the Kamada-Kawai algorithm.
 * </p>
 */
public class KKLayoutFactory implements ILayoutFactory{

    @Override
    public INetworkGraphLayout transform(AbstractNetworkGraph aNetworkGraph) {
        return new KamadaKawaiLayout(aNetworkGraph);
    }
}
