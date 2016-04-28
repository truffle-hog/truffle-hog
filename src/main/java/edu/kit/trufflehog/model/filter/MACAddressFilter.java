package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.kit.trufflehog.model.network.InvalidMACAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *     //TODO document
 * </p>
 * @author Mark Giraud
 * @version 1.0
 */
public class MACAddressFilter implements IFilter {

    private final Set<MacAddress> addresses = new HashSet<>();
    private final INetworkIOPort networkIOPort;
    private final Color filterColor;
    private final String name;
    private int priority = 0;

    /**
     * //TODO document
     *
     * @param networkIOPort the network IO port. This is needed to enable this filter to revert the chaanges it made to the nodes.
     * @param filterInput the filter input to input to this filter.
     * @throws InvalidFilterRule this exception is thrown if the filterInput contains invalid rules
     */
    public MACAddressFilter(INetworkIOPort networkIOPort, final FilterInput filterInput) throws InvalidFilterRule {

        if (networkIOPort == null)
            throw new NullPointerException("networkIOPort must not be null!");

        if (filterInput == null)
            throw new NullPointerException("filterInput must not be null!");

        if (filterInput.getType() != FilterType.MAC)
            throw new InvalidFilterRule("The filter input contains invalid filter rules. This filter can only handle mac rules");

        this.networkIOPort = networkIOPort;
        filterColor = filterInput.getColor();
        priority = filterInput.getPriority();
        name = filterInput.getName();

        final List<String> rules = filterInput.getRules();

        if (rules == null) {
            throw new NullPointerException("the rules list in filterInput must not be null!");
        }


        final List<MacAddress> macAddresses = new LinkedList<>();

        for (String rule : rules) {
            if (!rule.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
                throw new InvalidFilterRule("Mac address doesn't have the correct format");
            }

            final String[] macAddressParts = rule.split(":");

            long macAddress = 0;

            for (int i = 0; i < 6; i++) {
                int b = Integer.parseInt(macAddressParts[i], 16);
                macAddress <<= 8;
                macAddress += b;
            }

            try {
                macAddresses.add(new MacAddress(macAddress));
            } catch (InvalidMACAddress invalidMACAddress) {
                // nothing to do here because we can't pass invalid addresses.
            }
        }

        macAddresses.stream().forEach(addresses::add);

    }

    @Override
    public void clear() {

        //TODO maybe optimize so that we only iterate over changed nodes?
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
    public void check(final INode node) {
        if (node == null)
            throw new NullPointerException("node must not be null!");

        //TODO maybe null check?
        final MacAddress address = node.getComponent(NodeInfoComponent.class).getMacAddress();

        if (addresses.contains(address)) {
            node.getComponent(FilterPropertiesComponent.class).addFilterColor(this, filterColor);
        }
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(IFilter other) {
        return priority - other.getPriority();
    }
}
