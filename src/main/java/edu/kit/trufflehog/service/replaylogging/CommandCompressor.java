package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import org.apache.commons.collections4.map.LinkedMap;

import java.time.Instant;

/**
 * <p>
 *     The CommandCompressor creates a compacted list of all commands it received by using the {@link CommandCompressor}.
 *     For example, if there are 50 consecutive commands in the list that all increment the same counter by 1, these 50
 *     commands are packaged into 1 command that increments the counter by 50 and then returned as a single command.
 * </p>
 */
public class CommandCompressor {

    /**
     * <p>
     *     Creates a new CommandCompressor object.
     * </p>
     */
    public CommandCompressor() {
    }

    /**
     * <p>
     *     Creates a compacted list of all commands it received the way it has been described in the class description.
     * </p>
     *
     * @param commands The commands to compress.
     * @return The compressed commands.
     */
    public LinkedMap<ICommand, Instant> compressCommands(LinkedMap<ICommand, Instant> commands) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
