package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.configdata.ConfigDataModel;
import edu.kit.trufflehog.service.replaylogging.ReplayLogger;

/**
 * <p>
 *     Command supposed to change the time interval of taking snapshots by the {@link ReplayLogger}.
 * </p>
 */
public class SetLoggingIntervalCommand implements IUserCommand<Void> {
    private final ConfigDataModel configModel;

    /**
     * <p>
     *     Creates new command and provides config object.
     * </p>
     * @param config {@link ConfigDataModel} to manipulate
     */
    SetLoggingIntervalCommand(final ConfigDataModel config) {
        configModel = config;
    }

    @Override
    public void execute() {

    }

    @Override
    public <S extends Void> void setSelection(S selection) {
        throw new UnsupportedOperationException("You shouldn't be doing this!");
    }
}
