package edu.kit.trufflehog.model.graph;

import org.apache.commons.collections15.Transformer;

/**
 * Created by root on 24.01.16.
 */
public class FRLayoutFactory implements
        Transformer<ANetworkGraph, INetworkGraphLayout> {

    @Override
    public INetworkGraphLayout transform(ANetworkGraph iNetworkGraph) {

        return new FruchtermanReingoldLayout(iNetworkGraph);
    }
}
