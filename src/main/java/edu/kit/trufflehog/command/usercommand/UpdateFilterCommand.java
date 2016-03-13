package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.filter.MacroFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;

/**
 * <p>
 *     Command to update the graph after a newly added filter is enbaled
 * </p>
 */
public class UpdateFilterCommand implements IUserCommand<Void> {
    private final IFilter newFilter;
    private final IFilter oldFilter;
    private final MacroFilter macroFilter;
    private final INetworkWritingPort nwp;

    /**
     * //TODO document
     * @param nwp
     * @param newFilter
     * @param oldFilter
     * @param macroFilter
     */
    UpdateFilterCommand(final INetworkWritingPort nwp, final IFilter newFilter, final IFilter oldFilter, final MacroFilter macroFilter) {
        if (newFilter == null)
            throw new NullPointerException("newFilter must not be null!");

        if (oldFilter == null)
            throw new NullPointerException("oldFilter must not be null!");

        if(macroFilter == null)
            throw new NullPointerException("macroFilter must not be null!");

        this.newFilter = newFilter;
        this.oldFilter = oldFilter;
        this.macroFilter = macroFilter;
        this.nwp = nwp;
    }

    @Override
    public void execute() {
        macroFilter.removeFilter(oldFilter);
        macroFilter.addFilter(newFilter);
        nwp.applyFilter(newFilter);
    }

    @Override
    public <S extends Void> void setSelection(S selection) {
        throw new UnsupportedOperationException("Please stop!");
    }
}
