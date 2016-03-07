/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    public FilterInput(final String name, final List<String> rules, final Color color) {
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
