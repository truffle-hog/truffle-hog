package edu.kit.trufflehog.command.trufflecommand;

import java.time.Instant;

/**
 * <p>
 *     Command to start the spp_profinet Snort plugin.
 * </p>
 */
public class PluginNotRunningCommand implements ITruffleCommand {
    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Instant createdAt() {
        return null; //TODO implement
    }
}
