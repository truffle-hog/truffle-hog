package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.filter.*;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import javafx.scene.control.SelectionModel;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Command to update the graph after a newly added filter is enbaled
 * </p>
 */
public class UpdateFilterCommand implements IUserCommand<FilterInput> {
    private final MacroFilter macroFilter;
    private final INetworkWritingPort nwp;
    private FilterInput filterInput;

    private final Map<FilterInput, IFilter> filterMap = new HashMap<>();

    /**
     * //TODO document
     * @param nwp
     * @param macroFilter
     */
    public UpdateFilterCommand(final INetworkWritingPort nwp, final MacroFilter macroFilter) {
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
            }

            if (!filterInput.isActive()) {
                return;
            }

            IFilter filter = null;

            try {
                switch (filterInput.getOrigin()) {
                    case MAC:
                        filter = new MACAddressFilter(filterInput);
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
