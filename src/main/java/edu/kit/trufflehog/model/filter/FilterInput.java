package edu.kit.trufflehog.model.filter;

import java.awt.*;
import java.util.List;

/**
 * <p>
 *     The FilterInput class contains the data necessary to create a filter. From this class a filter can be created.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterInput {
    private List<String> rules;
    private Color color;

    /**
     * <p>
     *     Creates a new FilterInput object.
     * </p>
     *
     * @param rules The rules that define this filter.
     * @param color The color that a node should become if it matches with the filter.
     */
    public FilterInput(List<String> rules, Color color) {
        this.rules = rules;
        this.color = color;
    }
}
