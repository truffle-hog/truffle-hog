package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.*;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Command to update the graph when a filter is added, changes, or is deleted.
 * </p>
 *
 * @author Mark Giraud
 * @version 1.0
 */
public class UpdateFilterCommand implements IUserCommand<FilterInput> {

    private static final Logger logger = LogManager.getLogger();

    private final MacroFilter macroFilter;
    private final INetworkIOPort nwp;
    private final ConfigData configData;
    private FilterInput filterInput;

    private final Map<FilterInput, IFilter> filterMap;

    /**
     * <p>
     *     Constructs the update filter command. This command always needs a network io port to apply the newly added
     *     filters and pass the port to the constructed filter, which needs the port to clean up, when it is removed.
     * </p>
     *
     * @param nwp the network port that is used to access the network
     * @param macroFilter the macro filter to add all sub filters to.
     */
    public UpdateFilterCommand(final ConfigData configData, final INetworkIOPort nwp, final MacroFilter macroFilter, final Map<FilterInput, IFilter> filterMap)
    {

        if(macroFilter == null) throw new NullPointerException("macroFilter should not be null!");
        if (nwp == null) throw new NullPointerException("NetworkIOPort should not be null");
        if (filterMap == null) throw new NullPointerException("filtermap must not be null");
        if (configData == null) throw new NullPointerException("configdata must not be null");

        this.filterMap = filterMap;
        this.macroFilter = macroFilter;
        this.configData = configData;
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

            if (!filterInput.isActive() || filterInput.isDeleted()) {
                return;
            }

            IFilter filter = null;

            try {
                switch (filterInput.getType()) {
                    case MAC:
                        filter = new MACAddressFilter(nwp, filterInput);
                        break;
                    case IP:
                        filter = new IPAddressFilter(nwp, filterInput);
                        break;
                    case NAME:
                        filter = new NameRegexFilter(nwp, filterInput);
                        break;
                    default:
                        filter = IFilter.EMPTY;
                        break;
                }
            } catch (InvalidFilterRule invalidFilterRule) {
                logger.warn(invalidFilterRule);
                //TODO do some error handling and notify user maybe?
            }

            logger.debug("adding filter to filtermap, macrofilter, and apply the new filter");
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
