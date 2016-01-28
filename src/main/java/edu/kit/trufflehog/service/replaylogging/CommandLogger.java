package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * <p>
 *     The CommandLogger takes a list of commands, packages them into a more "dense" list through the
 *     {@link CommandCompressor}. This compacted list is returned to the {@link ReplayLogLoadService} where the command
 *     list is packaged into a {@link ReplayLog}.
 * </p>
 */
public class CommandLogger {

    /**
     * <p>
     *     Creates a new CommandLogger object.
     * </p>
     */
    public CommandLogger() {
    }

    /**
     * <p>
     *     Adds a command to the internal command list. The list will be taken by {@link #createCommandLog()}
     *     and then cleared.
     * </p>
     *
     * @param command The command to add to the list that is to be processed.
     */
    public void addCommand(ICommand command) {
    }

    /**
     * <p>
     *     Creates a compacted list of all commands it received by using the {@link CommandCompressor}.
     * </p>
     * <p>
     *     Once executed, all storred commands are deleted again.
     * </p>
     *
     * @return A list of compacted commands it received.
     */
    public List<ICommand> createCommandLog() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
