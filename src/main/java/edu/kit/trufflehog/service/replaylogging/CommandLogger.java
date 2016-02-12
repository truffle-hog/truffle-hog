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
     *     and then swapped.
     * </p>
     *
     * @param command The command to add to the list that is to be processed.
     * @param startInstant The instant at which the recording session was started
     */
    public void addCommand(ICommand command, Instant startInstant) {
        // TODO: subtract truffle arrival from startInstant
        waitingCommandMap.put(command, startInstant);
    }

    /**
     * <p>
     *     Swaps the waitingCommandMap list with the compressCommandMap.
     * </p>
     * <p>
     *     This is done for concurrency reasons. That way commands can be added to the waitingCommandList while the
     *     compressCommandList is being compressed.
     * </p>
     */
    public void swapMaps() {
        LinkedMap<ICommand, Instant> tempMap = commpressCommandMap;
        commpressCommandMap = waitingCommandMap;
        waitingCommandMap = tempMap;
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
