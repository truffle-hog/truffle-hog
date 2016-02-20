package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.Filter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;

import java.util.List;

/**
 * <p>
 *     Command used to run filters on a graph. Can be used to update the graph after filter preferences have changed.
 * </p>
 */
public class UpdateFilterCommand implements IUserCommand {

    // TODO make those final and not null as default!!!!!
    private INetworkWritingPort network = null;
    private List<Filter> filterList = null;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check.
     * </p>
     * @param port {@link INetworkWritingPort} to write data to
     * @param filters List of filters to check
     */
    UpdateFilterCommand(INetworkWritingPort port, List<Filter> filters) {
        network = port;
        filterList = filters;
    }

    @Override
    public void execute() {

    }
}
