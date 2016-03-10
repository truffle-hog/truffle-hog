package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.util.IListener;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 *     The ReplayLogSaveService saves the current graph state so that it can be fully reconstructed at a later date. It
 *     does this by creating {@link ReplayLog} objects at a fixed time interval (each ReplayLog object covers the data for
 *     this time interval). Thus the ReplayLogSaveService runs in its own thread.
 * </p>
 */
public class ReplayLogSaveService implements Runnable, IListener<ICommand> {
    private List<ICommand> commandList;
    private ExecutorService executorService;

    /**
     * <p>
     *     Creates a new ReplayLogSaveService object.
     * </p>
     */
    public ReplayLogSaveService() {
        commandList = new LinkedList<>();
    }

    /**
     * <p>
     *     Starts the replay log save service.
     * </p>
     * <p>
     *     The ReplayLogSaveService saves the current graph state so that it can be fully reconstructed at a later date.
     *     It does this by creating {@link ReplayLog} objects at a fixed time interval (each ReplayLog object covers the
     *     data for this time interval). Thus the ReplayLogSaveService runs in its own thread.
     * </p>
     */
    @Override
    public void run() {
    }

    /**
     * <p>
     *     Through this method the ReplayLogSaveService receives new Commands that it should save into the list so that
     *     they can be turned into {@link ReplayLog}s later.
     * </p>
     *
     * @param command The command to save
     */
    @Override
    public void receive(ICommand command) {

    }
}
