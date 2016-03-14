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

import com.google.common.collect.Range;
import edu.kit.trufflehog.model.network.graph.INode;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     
 * </p>
 *
 * @author Mark Giraud
 * @version 0.1
 */
public class IPFilter implements IFilter {

    final List<Range> ipRanges = new LinkedList<>();

    public void addInput(final FilterInput filterInput) {

    }

    @Override
    public void check(final INode node) {

    }

    @Override
    public int getPriority() {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public void clear() {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public Color getFilterColor(INode node) {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public int compareTo(IFilter o) {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }
}
