package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.*;
import edu.kit.trufflehog.model.network.INetworkIOPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * <p>
 *     The AddFilterCommand adds a new filter to the graph and to the database.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class AddFilterCommand implements IUserCommand<FilterInput> {

    private static final Logger logger = LogManager.getLogger();

    private FilterInput filterInput;
    private final ConfigData configData;

    private final INetworkIOPort nwp;
    private final MacroFilter macroFilter;
    private final Map<FilterInput, IFilter> filterMap;

    /**
     * <p>
     *     Creates a new AddFilterCommand that is used to a new filter to the graph and to the database.
     * </p>
     *  @param configData The config data that is used to access the database and store the filter there.
     * @param nwp The network port that is used to access the network.
     * @param macroFilter The macro filter to add all sub filters to.
     * @param filterMap
     */
    public AddFilterCommand(final ConfigData configData, final INetworkIOPort nwp, final MacroFilter macroFilter, Map<FilterInput, IFilter> filterMap) {

        this.nwp = nwp;
        this.macroFilter = macroFilter;
        this.filterMap = filterMap;

        this.configData = configData;
    }

    @Override
    public <S extends FilterInput> void setSelection(S selection) {
        this.filterInput = selection;
    }

    @Override
    public void execute() {



        if (filterInput != null) {


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
            configData.addFilterInput(filterInput);
        }
    }
}