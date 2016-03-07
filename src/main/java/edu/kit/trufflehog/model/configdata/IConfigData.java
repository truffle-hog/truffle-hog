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

package edu.kit.trufflehog.model.configdata;

import edu.kit.trufflehog.model.filter.FilterInput;
import javafx.beans.property.StringProperty;

import java.util.Properties;

/**
 * <p>
 *     Through the IConfigData interface it is possible to get any configuration that is required by TruffleHog
 *     at the start. These configurations could be settings from the settings menu, previously configured filters
 *     etc.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface IConfigData {
    /**
     * <p>
     *     Gets the value mapped to the given key in the currently loaded {@link Properties} object, or null if the key
     *     does not exist.
     * </p>
     *
     * @param key The key of the value to get in the currently loaded {@link Properties} object.
     * @return The value mapped to the key, if it exists, else null.
     */
    StringProperty getSetting(Class classType, String key);

    /**
     * <p>
     *     Gets the filter input object that is mapped to the given key.
     * </p>
     *
     * @param key The key that belongs to the filter that should be retrieved.
     * @return The filter input object that is mapped to the given key.
     */
    FilterInput getFilter(String key);
}
