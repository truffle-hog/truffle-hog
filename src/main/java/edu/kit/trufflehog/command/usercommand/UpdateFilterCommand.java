package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>
 *     Command to update the graph after a newly added filter is enbaled
 * </p>
 */
public class UpdateFilterCommand implements IUserCommand<Void> {
    private final IFilter filter;
    private final INetworkWritingPort nwp;

    /**
     * <p>
     *     Constructor taking the given new filter
     * </p>
     * @param filter filter to be used now
     * @param nwp graph port to use filer upon
     */
    UpdateFilterCommand(final INetworkWritingPort nwp, final IFilter filter) {
        this.filter = filter;
        this.nwp = nwp;
    }

    @Override
    public void execute() {
        nwp.applyFilter(filter);
    }

    @Override
    public <S extends Void> void setSelection(S selection) {
        throw new UnsupportedOperationException("Please stop!");
    }
}
