package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.command.ICommand;

/**
 * <p>
 *     Interface for view generated commands.
 * </p>
 */
public interface IUserCommand<T> extends ICommand {

    <S extends T> void setSelection(S selection);
}
