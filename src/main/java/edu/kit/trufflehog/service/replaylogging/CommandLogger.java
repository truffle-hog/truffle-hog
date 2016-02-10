package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     The CommandLogger takes a list of commands, packages them into a more "dense" list through the
 *     {@link CommandCompressor}. This compacted list is returned to the {@link ReplayLogLoadService} where the command
 *     list is packaged into a {@link ReplayLog}.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class CommandLogger {
    private List<ICommand> waitingCommandList; // This is where commands are put
    private List<ICommand> commpressCommandList; // This is where commands are compressed
    private CommandCompressor commandCompressor;

    /**
     * <p>
     *     Creates a new CommandLogger object.
     * </p>
     */
    public CommandLogger() {
        waitingCommandList = new LinkedList<>();
        commandCompressor = new CommandCompressor();
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
        waitingCommandList.add(command);
    }

    /**
     * <p>
     *     Transfers all commands from the waitingCommandList into the commpressCommandList where they can be compressed,
     *     and then empties the waitingCommandList.
     * </p>
     * <p>
     *     This is done for concurrency reasons. That commands can be added to the waitingCommandList while the
     *     commpressCommandList is being compressed.
     * </p>
     */
    public void transferList() {
        commpressCommandList = new LinkedList<>(waitingCommandList);
        waitingCommandList.clear();
    }

    /**
     * <p>
     *     Creates a compacted list of all commands it received by using the {@link CommandCompressor}.
     * </p>
     * <p>
     *     Once executed, all stored commands are deleted again.
     * </p>
     *
     * @return A list of compacted commands it received.
     */
    public List<ICommand> createCommandLog() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
