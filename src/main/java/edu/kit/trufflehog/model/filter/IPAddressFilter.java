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
import com.sun.javaws.exceptions.InvalidArgumentException;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.InvalidIPAddress;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;

import java.awt.*;
import java.util.List;

/**
 * <p>
 *     
 * </p>
 *
 * @author Mark Giraud
 * @version 1.0
 */
public class IPAddressFilter implements IFilter {

    final RangeSet<IPAddress> addresses = TreeRangeSet.create();
    private final INetworkIOPort networkIOPort;
    private final Color filterColor;
    private final String name;
    private int priority = 0;

    public IPAddressFilter(INetworkIOPort networkIOPort, final FilterInput filterInput) throws InvalidFilterRule {
        if (networkIOPort == null)
            throw new NullPointerException("networkIOPort must not be null!");

        if (filterInput == null)
            throw new NullPointerException("filterInput must not be null!");

        if (filterInput.getOrigin() != FilterOrigin.IP)
            throw new InvalidFilterRule("The filter input contains invalid filter rules. This filter can only handle ip rules");

        this.networkIOPort = networkIOPort;
        filterColor = filterInput.getColor();
        priority = filterInput.getPriority();
        name = filterInput.getName();

        final List<String> rules = filterInput.getRules();

        if (rules == null) {
            throw new NullPointerException("the rules list in filterInput must not be null!");
        }

        for (String rule : rules) {
            if (!rule.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\/(([1-2][0-9])|([1-9])|(3[0-2])))?$")) {
                throw new InvalidFilterRule("IP address doesn't have the correct format!");
            }

            if (rule.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
                final String[] parts = rule.split("\\.");

                try {
                    addresses.add(Range.singleton(new IPAddress(parseIP(parts))));
                } catch (InvalidIPAddress invalidIPAddress) {
                    // nothing to do here because the ip address has to be valid due to the regex above.
                }
            } else {
                final String[] masks = rule.split("/");
                final String[] parts = masks[0].split("\\.");
                final long ip = parseIP(parts);
                final long subnetMaskCount = Integer.parseInt(masks[1]);

                final long deviceMask = (1 << (32 - subnetMaskCount)) - 1;
                final long subnetMask = ((long) ((1 << subnetMaskCount) - 1)) << (32 - subnetMaskCount);
                final long from = ip & subnetMask;
                final long to = ip | deviceMask;

                try {
                    addresses.add(Range.closed(new IPAddress(from), new IPAddress(to)));
                } catch (InvalidIPAddress invalidIPAddress) {
                    // this should never happen but just in case notify user
                    invalidIPAddress.printStackTrace();
                    throw new InvalidFilterRule("IP address doesn't have the correct format or a parsing error occurred!");
                }
            }
        }
    }

    private long parseIP(String[] parts) {
        long ip = 0;

        for (String part : parts) {
            ip <<= 8;
            ip |= Integer.parseInt(part);
        }

        return ip;
    }

    @Override
    public void check(final INode node) {
        if (node == null)
            throw new NullPointerException("node must not be null!");

        final IPAddress address = node.getComponent(NodeInfoComponent.class).getIPAddress();

        if (address != null && addresses.contains(address)) {
            node.getComponent(FilterPropertiesComponent.class).addFilterColor(this, filterColor);
        }
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void clear() {
        networkIOPort.getNetworkNodes().stream().forEach(node -> {
            node.getComponent(FilterPropertiesComponent.class).removeFilterColor(this);
        });
    }

    @Override
    public Color getFilterColor() {
        return filterColor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(IFilter o) {
        return priority - o.getPriority();
    }
}
