package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>
 *     Command to add a filter created in GUI to the actual data base
 * </p>
 */
public class UpdateFilterCommand implements IUserCommand<Void> {
    private final ConfigDataModel cdm;
    private final FilterInput fi;

    /**
     * <p>
     *     Constructor taking the given informations in form of a FilterInput
     * </p>
     * @param fi filter input to create a filter out of
     * @param cdm ConfigDataModel to add filter to
     */
    UpdateFilterCommand(final ConfigDataModel cdm, final FilterInput fi) {
        this.fi = fi;
        this.cdm = cdm;
    }

    @Override
    public void execute() {
        cdm.addFilterInput(fi);
    }

    @Override
    public <S extends Void> void setSelection(S selection) {
        throw new UnsupportedOperationException("Please stop!");
    }
}
