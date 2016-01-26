package de.fraunhofer.iosb.spiff.trufflehog.service.datalogging;

import de.fraunhofer.iosb.spiff.trufflehog.command.ICommand;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * <p>
 *     The CommandLogger takes a list of commands, packages them into a more "dense" list through the
 *     {@link CommandCompressor}. That means if for instance there are 50 consecutive commands that all increment the
 *     same counter by 1, these 50 commands are packaged into 1 command that increments the counter by 50. This
 *     compacted list is returned to the {@link DataLogLoadService} where the command list is packaged into a
 *     {@link DataLog}.
 * </p>
 */
public class CommandLogger {

    /**
     * Creates a new CommandLogger object.
     */
    public CommandLogger() {
    }

    /**
     * Adds a command to the internal command list. The list will be taken by {@link #createCommandLog()}
     * and then cleared.
     *
     * @param command The command to add to the list that is to be processed.
     */
    public void addCommand(ICommand command) {
    }

    /**
     * <p>
     *     Creates a compacted list of all commands it received by using the {@link CommandCompressor}. For example, if
     *     there are 50 consecutive commands in the list that all increment the same counter by 1, these 50 commands are
     *     packaged into 1 command that increments the counter by 50 and then returned as a single command.
     * </p>
     * <p>
     *     Once executed, all storred commands are deleted again.
     * </p>
     *
     * @return A list of compacted commands it received.
     */
    public List<ICommand> createCommandLog() {
        throw new NotImplementedException();
    }
}
