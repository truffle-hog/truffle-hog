package edu.kit.trufflehog.model.filter;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     The FilterInput class contains the data necessary to create a filter. From this class a filter can be created.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterInput implements Serializable {
    private final String name;
    private final List<String> rules;
    private final Color color;

    /**
     * <p>
     *     Creates a new FilterInput object.
     * </p>
     *
     * @param name The name of this filter.
     * @param rules The rules that define this filter.
     * @param color The color that a node should become if it matches with the filter.
     */
    public FilterInput(String name, List<String> rules, Color color) {
        this.name = name;
        this.rules = rules;
        this.color = color;
    }

    /**
     * <p>
     *     Gets the name of this filter.
     * </p>
     *
     * @return the name of this filter.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     *     Gets the set of rules for this filter.
     * </p>
     *
     * @return the set of rules for this filter.
     */
    public List<String> getRules() {
        return rules;
    }

    /**
     * <p>
     *     Gets the color for this filter.
     * </p>
     *
     * @return the color for this filter.
     */
    public Color getColor() {
        return color;
    }
}
