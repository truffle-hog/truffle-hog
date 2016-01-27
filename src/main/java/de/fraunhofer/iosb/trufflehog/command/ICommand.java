package de.fraunhofer.iosb.trufflehog.command;

import java.io.Serializable;

/**
 *
 */
public interface ICommand extends Serializable {
    void execute();
}
