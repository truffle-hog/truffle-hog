package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.MacroFilter;
import edu.kit.trufflehog.model.network.INetworkIOPort;

/**
 * <p>
 *     The RemoveFilterCommand removes an existing filter from the graph and from the database.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class RemoveFilterCommand extends UpdateFilterCommand {
    private FilterInput filterInput;
    private final ConfigData configData;

    /**
     * <p>
     *     Creates a new RemoveFilterCommand that removes an existing filter from the graph and from the database.
     * </p>
     *
     * @param configData The config data that is used to access the database and remove the filter there.
     * @param nwp The network port that is used to access the network.
     * @param macroFilter The macro filter to add all sub filters to.
     */
    public RemoveFilterCommand(final ConfigData configData, final INetworkIOPort nwp, final MacroFilter macroFilter) {
        super(nwp, macroFilter);
        this.configData = configData;
    }

    @Override
    public <S extends FilterInput> void setSelection(S selection) {
        this.filterInput = selection;
    }

    @Override
    public void execute() {
        super.execute();
        if (filterInput != null) {
            configData.removeFilterInput(filterInput);
        }
    }
}