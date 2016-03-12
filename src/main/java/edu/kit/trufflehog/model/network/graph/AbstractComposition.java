package edu.kit.trufflehog.model.network.graph;

import javafx.beans.binding.ObjectBinding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * \brief An abstract implementation of the IComposition interface. Facilitates to create new IComposition classes.
 *
 * \date 04.03.16
 * \copyright GNU Public License
 * @author Jan Hermes
 * @version 0.0.1
 */
public abstract class AbstractComposition extends ObjectBinding<IComponent> implements IComposition {

    private static final Logger logger = LogManager.getLogger(AbstractComposition.class);
    
    final HashMap<Class<? extends IComponent>, IComponent> components = new HashMap<>();

    @Override
    protected void onInvalidating() {
        logger.debug("invalidated: " + name());
    }

    @Override
    public <T extends IComponent> T addComponent(T component) {

        final IComponent existing = components.get(component.getClass());

        if (existing != null) {
            // Safe to suppress unchecked as every value in the components
            // map that will be retrieved by the according class type will
            // be a component of that exact type
            @SuppressWarnings("unchecked")
            T castedExisting = (T) existing;
            return castedExisting;
        }
        components.put(component.getClass(), component);

        //logger.debug(components);
        // TODO do this differently? but i think null as indicator for "there was no previous value" is ok
        return null;
    }

    @Override
    public <T extends IComponent> T removeComponent(Class<T> type) {

        if (type == null) {
            throw new NullPointerException("componentType must not be null");
        }
        final IComponent component = components.remove(type);

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
    public int size() {
        return components.size();
    }

    @Override
    public boolean isEmpty() {
        return components.isEmpty();
    }

    @Override
    public boolean contains(Object o) {

        if (! (o instanceof IComponent)) {
            return false;
        }
        final IComponent other = (IComponent) o;
        return components.containsKey(other.getClass());
    }

    @Override
    public Iterator<IComponent> iterator() {
        return components.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return components.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return components.values().toArray(a);
    }

    @Override
    public boolean add(IComponent iComponent) {
        return this.addComponent(iComponent) == null;
    }

    @Override
    public boolean remove(Object o) {

        if (!(o instanceof IComponent)) {
            return false;
        }
        final IComponent toBeRemoved = (IComponent) o;

        return this.removeComponent(toBeRemoved.getClass()) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return components.values().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends IComponent> c) {

        boolean compositionWasModified = false;

        for (IComponent component : c) {

            compositionWasModified = this.addComponent(component) == null;
        }
        return compositionWasModified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        return components.values().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        return components.values().retainAll(c);
    }

    @Override
    public void clear() {

        components.values().clear();
    }

}
