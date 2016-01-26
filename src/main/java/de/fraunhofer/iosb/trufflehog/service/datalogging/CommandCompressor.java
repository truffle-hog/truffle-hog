package de.fraunhofer.iosb.trufflehog.service.datalogging;

import de.fraunhofer.iosb.trufflehog.command.ICommand;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author MrX
 */
public class CommandCompressor {

    /**
     * <p>
     *     Creates a compacted list of all commands it received by using the {@link CommandCompressor}. For example, if
     *     there are 50 consecutive commands in the list that all increment the same counter by 1, these 50 commands are
     *     packaged into 1 command that increments the counter by 50 and then returned as a single command.
     * </p>
     *
     * @param commands The commands to compress.
     * @return The compressed commands.
     */
    public List<ICommand> compressCommands(List<ICommand> commands) {
        throw new NotImplementedException();
    }
}
