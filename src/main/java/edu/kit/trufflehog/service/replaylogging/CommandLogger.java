package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
class CommandLogger {
    private static final Logger logger = LogManager.getLogger(CommandLogger.class);

    private LinkedMap<IReplayCommand, Long> waitingCommandMap; // This is where commands are put
    private LinkedMap<IReplayCommand, Long> commpressCommandMap; // This is where commands are compressed
    private final CommandCompressor commandCompressor;

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
    public void addCommand(IReplayCommand command, Instant startInstant) {
        long receivedAt = command.createdAt().toEpochMilli() - startInstant.toEpochMilli();

        if (receivedAt > 0) {
            waitingCommandMap.put(command, receivedAt);
        } else {
            logger.debug("Command created before logging was started, dropping command");
        }
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
        LinkedMap<IReplayCommand, Long> tempMap = commpressCommandMap;
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
    public LinkedMap<IReplayCommand, Long> createCommandLog() {
        return commandCompressor.compressCommands(commpressCommandMap);
    }
}
