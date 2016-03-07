package edu.kit.trufflehog.model.network.graph;

/**
 * Created by Hoehler on 06.03.2016.
 * TODO Implement this thing
 */
public class SimpleComposition extends AbstractComposition {
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
    public boolean update(INode node) {
        return false;
    }
}
