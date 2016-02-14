package edu.kit.trufflehog.command;

import java.time.Instant;

/**
 * <p>
 *     This type of command can be recorded using the replay logger.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface IReplayCommand extends ICommand {

    /**
     * <p>
     *     Gets the time instant that this command was created at.
     * </p>
     *
     * @return The instant that this command was created at.
     */
    Instant createdAt();
}
