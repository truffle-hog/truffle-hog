package edu.kit.trufflehog.model.network.graph.components.node;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.AbstractComponent;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>
 *     This class contains all flags and rendering properties that are set by filters when the node is filtered by them.
 * </p>
 * @author Mark Giraud
 * @version 1.0
 */
public class FilterPropertiesComponent extends AbstractComponent implements IComponent {

    private final Logger logger = LogManager.getLogger();

    private final TreeMultimap<IFilter, Color> filterColors = TreeMultimap.create(Ordering.natural(), Ordering.arbitrary());

    public boolean getHasColor() {
        return hasColor.get();
    }

    public BooleanProperty hasColorProperty() {
        return hasColor;
    }

    private final BooleanProperty hasColor = new SimpleBooleanProperty(false);

    private final ObjectProperty<Color> activeColor = new SimpleObjectProperty<>(null);
    /**
     * <p>
     *     This method maps the specified color to the filter. The colors are ordered by the filters priority.
     *     The color the node should be rendered with can be fetched with getFilterColor().
     * </p>
     * @param filter the filter the color originates from
     * @param color the color to map to the filter. The color from the filter with
     *              highest priority will be returned by getFilterColor()
     */
    synchronized public void addFilterColor(IFilter filter, Color color) {
        filterColors.put(filter, color);

        activeColor.set(getFilterColor());
        hasColor.set(true);
        logger.debug("adding filter color: " + color);
    }

    /**
     * <p>
     *     This method adds the specified filter colors to the existing map.
     * </p>
     * @param newFilterColors the new filter colors to add to the existing map
     */
    synchronized public void addFilterColors(Multimap<IFilter, Color> newFilterColors) {
        filterColors.putAll(newFilterColors);

        if (!newFilterColors.isEmpty()) {

            activeColor.set(getFilterColor());
            hasColor.set(true);
            logger.debug("add filter colors: " + newFilterColors);

        }
    }

    /**
     * <p>
     *     This method removes the color set by the supplied filter at some time.
     * </p>
     * @param filter the filter whose color should be removed
     */
    synchronized public void removeFilterColor(final IFilter filter) {
        filterColors.remove(filter, filter.getFilterColor());

        activeColor.set(getFilterColor());

        hasColor.set(!filterColors.isEmpty());

        logger.debug("remove filter color: " + filter);
    }

    /**
     * <p>
     *     This method gets the current filter color that should be rendered.
     * </p>
     * @return the color this node should be rendered with. If no color is specified this method returns null.
     */
    synchronized public Color getFilterColor() {

        if (filterColors.isEmpty()) {
            return null;
        }

        return filterColors.get(filterColors.keySet().last()).first();
    }

    public Color getActiveColor() {
        return activeColor.get();
    }

    public ObjectProperty<Color> activeColorProperty() {
        return activeColor;
    }

    /**
     * This method returns an unmodifiable multimap of the filter colors.
     * @return the map of filter colors
     */
    public Multimap<IFilter, Color> getFilterColors() {
        return Multimaps.unmodifiableMultimap(filterColors);
    }

    @Override
    public String name() {
        return "Filter properties";
    }

    @Override
    public <T> T accept(IComponentVisitor<T> visitor) {
        return visitor.visit(this);
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
