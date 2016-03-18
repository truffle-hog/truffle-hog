package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.model.configdata.ConfigData;
import javafx.beans.property.Property;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     The FilterInput interface defines the bare minimum data necessary to create a filter. That means the following:
 *     <ul>
 *         <li>
 *             Name: The name of the filter. It has to be unique.
 *         </li>
 *         <li>
 *             Selection model: The selection type of the filter. A filter can either be based on a selection or on an
 *             inverse selection.
 *         </li>
 *         <li>
 *             Origin: The origin of the filter. A filter can originate from an IP Address, from a MAC Address, or
 *             from the current selection. This indicates upon what criteria the filter filters.
 *         </li>
 *         <li>
 *             Rules: The rules of the filter define what the filter matches. These are regular expressions matching
 *             IP addresses, MAC addresses and more.
 *         </li>
 *         <li>
 *             Priority: This priority is used to determine which filter color should be rendered when multiple
 *             filters collide on the same node.
 *         </li>
 *         <li>
 *             Color: The color of the filter determines what color a matched node should become.
 *         </li>
 *         <li>
 *             Authorized: If true, all nodes filtered by the filter will be considered as "good" or legal nodes. If set to
 *             false all nodes filtered by the filter will be considered as "evil" or illegal nodes.
 *         </li>
 *         <li>
 *             Active: Whether this filter is currently being applied on the network or not.
 *         </li>
 *     </ul>
 * </p>
 * <p>
 *     It used to serialize and deserialize filter inputs, thus when the implementation or filter input changes, the
 *     object can still be deserialized.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface IFilterInput extends Serializable {
    /**
     * <p>
     *     Gets the name of this filter. It has to be unique.
     * </p>
     *
     * @return The name of this filter.
     */
    String getName();

    /**
     * <p>
     *     Sets the name of this filter. It has to be unique.
     * </p>
     *
     * @param name The name of this filter.
     */
    void setName(String name);

    /**
     * <p>
     *    Gets the selection type of the filter. A filter can either be based on a selection or on an inverse selection.
     * </p>
     *
     * @return The selection type of this filter.
     */
    SelectionModel getSelectionModel();

    /**
     * <p>
     *     Gets the origin of the filter. A filter can originate from an IP Address, from a MAC Address, or from the
     *     current selection. This indicates upon what criteria the filter filters.
     * </p>
     *
     * @return The origin of this filter.
     */
    FilterOrigin getOrigin();

    /**
     * <p>
     *     Gets the set of rules for this filter. The rules of the filter define what the filter matches. These are
     *     regular expressions matching IP addresses, MAC addresses and more.
     * </p>
     *
     * @return The set of rules for this filter.
     */
    List<String> getRules();

    /**
     * <p>
     *     Gets the color for this filter. The color of the filter determines what color a matched node should
     *     become.
     * </p>
     *
     * @return The color for this filter.
     */
    Color getColor();

    /**
     * <p>
     *     Gets the authorized state of this filter. If it is set to true, all nodes filtered by the filter will be
     *     considered as "good" or legal nodes. If it is set to false all nodes filtered by the filter will be
     *     considered as "evil" or illegal nodes.
     * </p>
     *
     * @return The authorized state of this filter.
     */
    boolean isAuthorized();

    /**
     * <p>
     *     Gets the current activity state. That means this method returns true if the filter is currently being applied
     *     to the network, and otherwise false.
     * </p>
     *
     * @return True if the filter is currently being applied o the network, else false.
     */
    boolean isActive();

    /**
     * <p>
     *     Sets the set of rules for this filter. The rules of the filter define what the filter matches. These are
     *     regular expressions matching IP addresses, MAC addresses and more.
     * </p>
     *
     * @param rules The set of rules for this filter.
     */
    void setRules(List<String> rules);

    /**
     * <p>
     *     Gets the priority of the filter. This priority is used to determine which filter color should
     *     be rendered when multiple filters collide on the same node.
     * </p>
     *
     * @return the priority of the filter.
     */
    int getPriority();

    /**
     * <p>
     *     Since {@link Property} objects cannot be serialized, THIS METHOD HAS TO BE CALLED AFTER EACH
     *     DESERIALIZATION OF A FILTERINPUT OBJECT to recreate the connection between the Properties and the
     *     normal values that were serialized.
     * </p>
     *
     * @param configData The {@link ConfigData} object used to update this filter to the database.
     */
    void load(ConfigData configData);
}
