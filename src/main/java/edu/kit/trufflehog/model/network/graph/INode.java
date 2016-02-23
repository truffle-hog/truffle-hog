package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;

/**
 * <p>
 *     Interface used to represent nodes in the graph.
 * </p>
 */
public interface INode extends Comparable<INode>, IComponent {

    IAddress getAddress();

    INode createDeepCopy();

    <T extends IComponent> void addComponent(final T component);

    void removeComponent(Class<? extends IComponent> type);

    <T extends IComponent> T getComponent(final Class<T> componentType);


}
