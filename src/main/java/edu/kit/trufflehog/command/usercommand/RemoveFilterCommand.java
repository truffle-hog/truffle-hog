package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.filter.MacroFilter;
import edu.kit.trufflehog.model.network.INetworkIOPort;

import java.util.Map;

/**
 * <p>
 *     The RemoveFilterCommand removes an existing filter from the graph and from the database.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class RemoveFilterCommand implements IUserCommand<FilterInput> {
    private FilterInput filterInput;
    private final ConfigData configData;

    private final INetworkIOPort nwp;
    private final MacroFilter macroFilter;
    private final Map<FilterInput, IFilter> filterMap;

    /**
     * <p>
     *     Creates a new RemoveFilterCommand that removes an existing filter from the graph and from the database.
     * </p>
     *  @param configData The config data that is used to access the database and remove the filter there.
     * @param nwp The network port that is used to access the network.
     * @param macroFilter The macro filter to add all sub filters to.
     * @param filterMap
     */
    public RemoveFilterCommand(final ConfigData configData, final INetworkIOPort nwp, final MacroFilter macroFilter, Map<FilterInput, IFilter> filterMap) {
        this.configData = configData;

        this.nwp = nwp;
        this.macroFilter = macroFilter;
        this.filterMap = filterMap;
    }

    @Override
    public <S extends FilterInput> void setSelection(S selection) {
        this.filterInput = selection;
    }

    @Override
    public void execute() {

        if (filterInput != null) {

            if (filterMap.get(filterInput) != null) {
                macroFilter.removeFilter(filterMap.get(filterInput));
                filterMap.remove(filterInput);
            }

            configData.removeFilterInput(filterInput);
        }
    }
}