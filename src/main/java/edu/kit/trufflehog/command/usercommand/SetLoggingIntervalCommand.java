package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.service.replaylogging.ReplayLogger;
import javafx.beans.property.IntegerProperty;

/**
 * <p>
 *     Command supposed to change the time interval of taking snapshots by the {@link ReplayLogger}.
 * </p>
 */

public class SetLoggingIntervalCommand implements IUserCommand<Void> {
    private final ConfigDataModel configModel;

    /**
     * <p>
     *     Creates new command and provides the integer in a property object.
     * </p>
     * @param cdm Integer to update.
     */
    SetLoggingIntervalCommand(final ConfigDataModel cdm) {
        this.configModel = cdm;
    }

    @Override
    public void execute() {

    }

    @Override
    public <S extends Void> void setSelection(S selection) {
        throw new UnsupportedOperationException("You shouldn't be doing this!");
    }
}
