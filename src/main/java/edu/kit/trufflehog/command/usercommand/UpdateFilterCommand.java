package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.graph.INetworkGraph;
import edu.kit.trufflehog.model.network.INetworkWritingPort;

import java.util.List;

/**
 * <p>
 *     Command used to run filters on a graph. Can be used to update the graph after filter preferences have changed.
 * </p>
 */
public class UpdateFilterCommand implements IUserCommand {

    // TODO make those final and not null as default!!!!!
    private INetworkWritingPort networkGraph = null;
    private List<Filter> filterList = null;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check.
     * </p>
     * @param graph {@link INetworkGraph} to add data to
     * @param filters List of filters to check
     */
    UpdateFilterCommand(INetworkWritingPort graph, List<Filter> filters) {
        networkGraph = graph;
        filterList = filters;
    }

    @Override
    public void execute() {

    }
}
