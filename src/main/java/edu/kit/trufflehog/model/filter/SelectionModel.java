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

/**
 * <p>
 *     The SelectionModel determines what type a filter is. A filter can either be based on a selection or an inverse selection.
 *     That means all nodes matched by the filter either count as safe (whitelist) or unsafe (blacklist).
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public enum SelectionModel {
    SELECTION, INVERSE_SELECTION
}
