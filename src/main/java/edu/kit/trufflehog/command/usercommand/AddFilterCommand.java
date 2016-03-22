package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.MacroFilter;
import edu.kit.trufflehog.model.network.INetworkIOPort;

/**
 * <p>
 *     The AddFilterCommand adds a new filter to the graph and to the database.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class AddFilterCommand extends UpdateFilterCommand {
    private FilterInput filterInput;
    private final ConfigData configData;

    /**
     * <p>
     *     Creates a new AddFilterCommand that is used to a new filter to the graph and to the database.
     * </p>
     *
     * @param configData The config data that is used to access the database and store the filter there.
     * @param nwp The network port that is used to access the network.
     * @param macroFilter The macro filter to add all sub filters to.
     */
    public AddFilterCommand(final ConfigData configData, final INetworkIOPort nwp, final MacroFilter macroFilter) {
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
            configData.addFilterInput(filterInput);
        }
    }
}