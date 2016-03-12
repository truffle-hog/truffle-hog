package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.model.network.InvalidMACAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;

import java.util.*;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * @author Mark Giraud
 * @version 0.1
 */
public class MACAddressFilter implements IFilter {

    private final Set<MacAddress> addresses = new HashSet<>();
    private final Set<INode> changedNodes = new HashSet<>();

    @Override
    public void check(final INode node) {

        final MacAddress address = node.getComponent(NodeInfoComponent.class).getMacAddress();

        if (addresses.contains(address)) {
            //TODO add flags to node
            changedNodes.add(node);
        }
    }

    public void addInput(final FilterInput filterInput) throws InvalidFilterRule {

        //TODO add the rest of filter input to local data.

        List<String> rules = filterInput.getRules();
        List<MacAddress> macAddresses = new LinkedList<>();

        for (String rule : rules) {
            if (!rule.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"))
                throw new InvalidFilterRule("Mac address doesn't have the correct format");

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

    /**
     * <p>
     *     This method clears the filter of all rules and resets all flags set by the filter.
     * </p>
     */
    public void clear() {
        for (INode node : changedNodes) {
            //TODO reset flags
        }
    }
}
