package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import org.apache.commons.collections4.map.LinkedMap;

import java.time.Instant;

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
    private LinkedMap<ICommand, Instant> waitingCommandMap; // This is where commands are put
    private LinkedMap<ICommand, Instant> commpressCommandMap; // This is where commands are compressed
    private CommandCompressor commandCompressor;

    /**
     * <p>
     *     Creates a new CommandLogger object.
     * </p>
     */
    public CommandLogger() {
        waitingCommandMap = new LinkedMap<>();
        commpressCommandMap = new LinkedMap<>();
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
        waitingCommandMap.put(command, Instant.now());
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
        commpressCommandMap = new LinkedMap<>(waitingCommandMap);
        waitingCommandMap.clear();
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
    public LinkedMap<ICommand, Instant> createCommandLog() {
        return commandCompressor.compressCommands(commpressCommandMap);
    }
}
