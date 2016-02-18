package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.command.IReplayCommand;
import edu.kit.trufflehog.util.IListener;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public interface IReplayLogController {
    void startPlay(long instant, boolean strict);

    void stopPlay();

    void jumpToInstant(long instant, boolean strict);

    void startCapture();

    void stopCapture();

    IListener<IReplayCommand> asListener();

    void registerListener(IListener<IReplayCommand> listener);

    void unregisterListener(IListener<IReplayCommand> listener);
}
