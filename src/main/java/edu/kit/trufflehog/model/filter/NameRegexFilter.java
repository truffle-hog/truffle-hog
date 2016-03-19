package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *     This class implements a filter that filters nodes by their names using regexes defined by the user.
 * </p>
 * @author Mark Giraud
 * @version 1.0
 */
public class NameRegexFilter implements IFilter {

    private static final Logger logger = LogManager.getLogger();

    private final Set<Pattern> patterns = new HashSet<>();
    private final INetworkIOPort networkIOPort;
    private final Color filterColor;
    private final int priority;
    private final String name;
    private final SelectionModel selectionModel;

    public NameRegexFilter(INetworkIOPort networkIOPort, final FilterInput filterInput) throws InvalidFilterRule {
        if (networkIOPort == null)
            throw new NullPointerException("networkIOPort must not be null!");

        if (filterInput == null)
            throw new NullPointerException("filterInput must not be null!");

        if (filterInput.getOrigin() != FilterOrigin.NAME)
            throw new InvalidFilterRule("The filter input contains invalid filter rules. This filter can only handle name rules");

        if (filterInput.getSelectionModel() == null) {
            throw new IllegalArgumentException("selectionModel in filterInput must not be null!");
        }

        this.networkIOPort = networkIOPort;
        filterColor = filterInput.getColor();
        priority = filterInput.getPriority();
        name = filterInput.getName();
        selectionModel = filterInput.getSelectionModel();

        final java.util.List<String> rules = filterInput.getRules();

        if (rules == null) {
            throw new NullPointerException("the rules list in filterInput must not be null!");
        }

        patterns.addAll(rules.stream().map(Pattern::compile).collect(Collectors.toList()));
    }

    @Override
    public void check(INode node) {
        if (node == null)
            throw new NullPointerException("node must not be null!");

        final NodeInfoComponent nic = node.getComponent(NodeInfoComponent.class);
        if (nic == null) {
            logger.debug("NodeInfoComponent was null!");
        }

        final String deviceName = node.getComponent(NodeInfoComponent.class).getDeviceName();

        if (deviceName != null) {
            switch (selectionModel) {
                case SELECTION:
                    if (patterns.parallelStream().anyMatch(p -> p.matcher(deviceName).matches())) {
                        markNode(node);
                    }
                    break;

                case INVERSE_SELECTION:
                    if (!patterns.parallelStream().anyMatch(p -> p.matcher(deviceName).matches())) {
                        markNode(node);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private void markNode(INode node) {
        final FilterPropertiesComponent fpc = node.getComponent(FilterPropertiesComponent.class);
        if (fpc == null) {
            logger.debug("FilterPropertiesComponent doesn't exist!");
            return;
        }
        fpc.addFilterColor(this, filterColor);
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
