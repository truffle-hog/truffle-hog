package edu.kit.trufflehog.command;

import java.io.Serializable;

/**
 *
 */
public interface ICommand<S> extends Serializable {
    void execute();
}
