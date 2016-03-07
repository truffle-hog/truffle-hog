package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.service.replaylogging.ReplayLogger;
import javafx.beans.property.IntegerProperty;

/**
 * <p>
 *     Command supposed to change the time interval of taking snapshots by the {@link ReplayLogger}.
 * </p>
 */
public class SetLoggingIntervalCommand implements IUserCommand {
    private final IntegerProperty intprop;

    /**
     * <p>
     *     Creates new command and provides the integer in a property object.
     * </p>
     * @param intprop Integer to update.
     */
    SetLoggingIntervalCommand(final IntegerProperty intprop) {
        this.intprop = intprop;
    }

    @Override
    public void execute() {

    }
}
