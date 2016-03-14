package edu.kit.trufflehog.model.network.graph.components.node;

import com.google.common.collect.*;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.util.ICopyCreator;
import java.awt.*;

/**
 * //TODO document
 * @author Mark Giraud
 */
public class FilterPropertiesComponent implements IComponent {

    private final TreeMultimap<IFilter, Color> filterColors = TreeMultimap.create(Ordering.natural(), Ordering.arbitrary());

    public void addFilterColor(IFilter filter, Color color) {
        filterColors.put(filter, color);
    }

    /**
     * <p>
     *     This method removes the color set by the supplied filter at some time.
     * </p>
     * @param filter
     */
    public void removeFilterColor(final IFilter filter, final INode node) {
        filterColors.remove(filter, filter.getFilterColor(node));
    }

    /**
     * <p>
     *     This method gets the current filter color that should be rendered.
     * </p>
     */
    public Color getFilterColor() {

        //FIXME make this thread safe so we don't get null pointer exceptions

        if (filterColors.isEmpty()) {
            return null;
        }

        return filterColors.get(filterColors.keySet().last()).first();
    }

    public Multimap<IFilter, Color> getFilterColors() {
        return filterColors;
    }

    @Override
    public String name() {
        return "Filter properties";
    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        if (copyCreator == null) throw new NullPointerException("copyCreator must not be null!");
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FilterPropertiesComponent;
    }

    @Override
    public String toString() {
        return filterColors.toString();
    }
}
