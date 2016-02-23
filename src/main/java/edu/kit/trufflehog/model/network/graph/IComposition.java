package edu.kit.trufflehog.model.network.graph;

import java.util.Collection;

/**
 * Created by jan on 23.02.16.
 */
public interface IComposition extends IComponent {

    <T extends IComponent> void addComponent(final T component);

    void removeComponent(Class<? extends IComponent> type);

    <T extends IComponent> T getComponent(final Class<T> componentType);

    Collection<? extends IComponent> getComponents();
}
