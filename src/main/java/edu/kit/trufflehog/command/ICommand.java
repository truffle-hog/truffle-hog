package edu.kit.trufflehog.command;

import edu.kit.trufflehog.service.executor.CommandExecutor;

import java.io.Serializable;

/**
 * <p>
 *     This interface is used to encapsulate an operation and execute it by the {@link CommandExecutor} using its own
 *     scheduling policies. The operation to perform has to be implemented in the execute() method. All the references
 *     needed must be stored within the command class. If the operation requires too much computation time, it is
 *     advisable to start a new thread to prevent starvation of other commands.
 * </p>
 */
public interface ICommand<S> extends Serializable {

    /**
     * <p>
     *     Executes the operation
     * </p>
     */
    void execute();
}
