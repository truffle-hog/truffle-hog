package edu.kit.trufflehog.model.network.graph;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by jan on 23.02.16.
 */
public abstract class AbstractComposition implements IComposition {

    final HashMap<Class<? extends IComponent>, IComponent> components = new HashMap<>();

    @Override
    public <T extends IComponent> void addComponent(T component) {

        if (components.containsKey(component.getClass())) {
            throw new RuntimeException("the Node already maintains this component type,"
                    + "you cannot add two components of the same class name: " + component);
        }
        components.put(component.getClass(), component);
    }

    @Override
    public void removeComponent(Class<? extends IComponent> type) {

        components.remove(type);
    }

    @Override
    public <T extends IComponent> T getComponent(Class<T> componentType) {

        if (componentType == null) {
            throw new NullPointerException("componentType must not be null");
        }

        final IComponent component = components.get(componentType);

        if (component != null) {

            // Safe to suppress unchecked as every value in the components
            // map that will be retrieved by the according class type will
            // be a component of that exact type
            @SuppressWarnings("unchecked")
            T existing = (T) component;

            return existing;
        } else {
            return null;
        }
    }

    @Override
    public Collection<? extends IComponent> getComponents() {
        return components.values();
    }
}
