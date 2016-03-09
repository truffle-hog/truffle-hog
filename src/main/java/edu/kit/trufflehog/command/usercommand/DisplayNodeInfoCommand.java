package edu.kit.trufflehog.command.usercommand;

/**
 * <p>
 *     Command to display statistics of a certain node in the graph.
 * </p>
 */
public class DisplayNodeInfoCommand implements IUserCommand<Void> {

    @Override
    public void execute() {
    }

    @Override
    public <S extends Void> void setSelection(S selection) {
        throw new UnsupportedOperationException("Don't do it");
    }
}
