package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;

import java.util.List;

/**
 * <p>
 *     Command used to run filters on a graph. Can be used to update the graph after filter preferences have changed.
 * </p>
 */
public class UpdateFilterCommand implements IUserCommand<Void> {

    private final INetworkWritingPort network;
    private final List<IFilter> filterList;

    /**
     * <p>
     *     Creates new command, provides a graph to work on and the filters to check.
     * </p>
     * @param port {@link INetworkWritingPort} to write data to
     * @param filters List of filters to check
     */
    UpdateFilterCommand(final INetworkWritingPort port, final List<IFilter> filters) {
        network = port;
        filterList = filters;
    }

    @Override
    public void execute() {

    }

    @Override
    public <S extends Void> void setSelection(S selection) {
        throw new UnsupportedOperationException("Please stop!");
    }
}
