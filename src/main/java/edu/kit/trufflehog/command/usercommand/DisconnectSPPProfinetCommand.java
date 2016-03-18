package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.TruffleReceiver;

/**
 * //TODO document
 * @author Mark Giraud
 * @version 0.1
 */
public class DisconnectSPPProfinetCommand implements IUserCommand {

    private final TruffleReceiver receiver;

    public DisconnectSPPProfinetCommand(final TruffleReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void setSelection(Object selection) {

    }

    @Override
    public void execute() {
        receiver.disconnect();
    }
}
