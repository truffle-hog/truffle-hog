package edu.kit.trufflehog.model.graph;

import org.apache.commons.collections15.Transformer;

/**
 * Created by Jan on 24.01.16.
 */
public class KKLayoutFactory implements
        Transformer<ANetworkGraph, INetworkGraphLayout> {

    @Override
    public INetworkGraphLayout transform(ANetworkGraph aNetworkGraph) {
        return new KamadaKawaiLayout(aNetworkGraph);
    }
}
