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
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.uci.ics.jung.graph.Tree;

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
public class IPAddressFilter implements IFilter {

    final RangeSet<IPAddress> ipRanges = TreeRangeSet.create();

    public IPAddressFilter(INetworkIOPort networkIOPort, final FilterInput filterInput) throws InvalidFilterRule {

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
    public Color getFilterColor() {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }

    @Override
    public int compareTo(IFilter o) {
        //TODO implement this
        throw new UnsupportedOperationException("Method not yet implemented!");
    }
}
