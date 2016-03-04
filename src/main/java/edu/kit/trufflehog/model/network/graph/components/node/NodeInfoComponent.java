package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IComponent;

/**
 * Created by root on 26.02.16.
 */
public class NodeInfoComponent implements IComponent {


    //public isMulticastAddress

    @Override
    public String name() {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public IComponent createDeepCopy() {
        return null;
    }

    @Override
    public boolean update(IComponent update) {
        return false;
    }
}
