package edu.kit.trufflehog.command.trufflecommand;

/**
 * <p>
 *     This command is used to notify the user of any errors that occurred in the receiver.
 * </p>
 */
public class ReceiverErrorCommand implements ITruffleCommand {

    final String message;

    public ReceiverErrorCommand(final String message) { //TODO replace the String with a language specific property?
        this.message = message;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
