package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.*;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import javafx.scene.control.SelectionModel;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Command to update the graph when a filter is added, changes, or is deleted.
 * </p>
 */
public class UpdateFilterCommand implements IUserCommand<FilterInput> {
    private final MacroFilter macroFilter;
    private final INetworkIOPort nwp;
    private FilterInput filterInput;

    private final Map<FilterInput, IFilter> filterMap = new HashMap<>();

    /**
     * <p>
     *     Constructs the update filter command. This command always needs a network io port to apply the newly added
     *     filters and pass the port to the constructed filter, which needs the port to clean up, when it is removed.
     * </p>
     * @param nwp the network port that is used to access the network
     * @param macroFilter the macro filter to add all sub filters to.
     */
    public UpdateFilterCommand(final INetworkIOPort nwp, final MacroFilter macroFilter) {
        if(macroFilter == null)
            throw new NullPointerException("macroFilter must not be null!");

        this.macroFilter = macroFilter;
        this.nwp = nwp;
        this.filterInput = null;
    }

    @Override
    public void execute() {

        if (filterInput != null) {

            if (filterMap.get(filterInput) != null) {
                macroFilter.removeFilter(filterMap.get(filterInput));
                filterMap.remove(filterInput);
            }

            if (!filterInput.isActive()) {
                return;
            }

            IFilter filter = null;

            try {
                switch (filterInput.getOrigin()) {
                    case MAC:
                        filter = new MACAddressFilter(nwp, filterInput);
                        break;
                    case IP:
                        filter = new IPFilter();
                        break;
                    default:
                        filter = IFilter.EMPTY;
                        break;
                }
            } catch (InvalidFilterRule invalidFilterRule) {
                //TODO do some error handling and notify user maybe?
            }

            filterMap.put(filterInput, filter);
            macroFilter.addFilter(filter);
            nwp.applyFilter(filter);
        }
    }

    @Override
    public <S extends FilterInput> void setSelection(S filterInput) {
        this.filterInput = filterInput;
    }
}
