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

import eu.hansolo.enzo.notification.Notification;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     The FilterInput class contains the data necessary to create a filter. From this class a filter can be created.
 *     That means the following:
 *     <ul>
 *         <li>
 *             Name: The name of the filter. It has to be unique.
 *         </li>
 *         <li>
 *             Type: The type of the filter. A filter can either be a whitelist or a blacklist, that means all nodes
 *             matched by the filter either count as safe (whitelist) or unsafe (blacklist).
 *         </li>
 *         <li>
 *             Rules: The rules of the filter define what the filter matches. These are regular expressions matching
 *             IP addresses, MAC addresses and more.
 *         </li>
 *         <li>
 *             Color: The color of the filter determines what color a matched node should become.
 *         </li>
 *     </ul>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterInput implements Serializable {
    private final String name;
    private final FilterType type;
    private final List<String> rules;
    private final Color color;
    private boolean active;
    private transient BooleanProperty booleanProperty;

    /**
     * <p>
     *     Creates a new FilterInput object that is inactive. That means it will at first not be applied onto the
     *     current network.
     * </p>
     * <p>
     *     <ul>
     *         <li>
     *             Name: The name of the filter. It has to be unique.
     *         </li>
     *         <li>
     *             Type: The type of the filter. A filter can either be a whitelist or a blacklist, that means all nodes
     *             matched by the filter either count as safe (whitelist) or unsafe (blacklist).
     *         </li>
     *         <li>
     *             Rules: The rules of the filter define what the filter matches. These are regular expressions matching
     *             IP addresses, MAC addresses and more.
     *         </li>
     *         <li>
     *             Color: The color of the filter determines what color a matched node should become.
     *         </li>
     *     </ul>
     * </p>
     *
     * @param name The name of this filter.
     * @param type The type of this filter.
     * @param rules The rules that define this filter.
     * @param color The color that a node should become if it matches with the filter.
     */
    public FilterInput(final String name, final FilterType type, final List<String> rules, final Color color) {
        this.name = name;
        this.type = type;
        this.rules = rules;
        this.color = color;
        this.active = false;

        load();
    }

    /**
     * <p>
     *     Gets the name of this filter. It has to be unique.
     * </p>
     *
     * @return the name of this filter.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     *     Gets the type of this filter. A filter can either be a whitelist or a blacklist, that means all nodes
     *     matched by the filter either count as safe (whitelist) or unsafe (blacklist).
     * </p>
     *
     * @return The type of this filter.
     */
    public FilterType getType() {
        return type;
    }

    /**
     * <p>
     *     Gets the set of rules for this filter. The rules of the filter define what the filter matches. These are
     *     regular expressions matching IP addresses, MAC addresses and more.
     * </p>
     *
     * @return the set of rules for this filter.
     */
    public List<String> getRules() {
        return rules;
    }

    /**
     * <p>
     *     Gets the color for this filter. The color of the filter determines what color a matched node should
     *     become.
     * </p>
     *
     * @return the color for this filter.
     */
    public Color getColor() {
        return color;
    }

    /**
     * <p>
     *     Gets the current activity state. That means this method returns true if the filter is currently being applied
     *     to the network, and otherwise false.
     * </p>
     *
     * @return True if the filter is currently being applied o the network, else false.
     */
    public boolean isActive() {
        return active;
    }

    public BooleanProperty getBooleanProperty() {
        return booleanProperty;
    }

    public void load() {
        booleanProperty = new SimpleBooleanProperty(active);
        booleanProperty.addListener((observable, oldValue, newValue) -> {
            active = newValue;

            if (newValue) {
                Notification.Notifier.INSTANCE.notifyInfo("Filter Active", name + " was just activated.");
            } else {
                Notification.Notifier.INSTANCE.notifyInfo("Filter Inactive", name + " was just deactivated.");
            }
        });
    }
}
