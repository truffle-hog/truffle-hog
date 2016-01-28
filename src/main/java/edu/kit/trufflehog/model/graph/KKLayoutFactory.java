package edu.kit.trufflehog.model.graph;

import org.apache.commons.collections15.Transformer;

/**
 * Created by Jan on 24.01.16.
 */
public class KKLayoutFactory implements
        Transformer<AbstractNetworkGraph, INetworkGraphLayout> {

    @Override
    public INetworkGraphLayout transform(AbstractNetworkGraph
                                                     abstractNetworkGraph) {
        return new KamadaKawaiLayout(abstractNetworkGraph);
    }
}
