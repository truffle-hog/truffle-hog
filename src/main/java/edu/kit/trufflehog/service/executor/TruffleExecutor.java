package edu.kit.trufflehog.service.executor;

import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.util.IListener;

import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by root on 25.02.16.
 */
public class TruffleExecutor implements Runnable, IListener<ITruffleCommand> {

    private final BlockingQueue<ITruffleCommand> blockingQueue = new LinkedBlockingDeque<>();

    @Override
    public void run() {

        while (!Thread.interrupted()) {

            try {
                blockingQueue.take().execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void receive(ITruffleCommand iTruffleCommand) {

        try {
            blockingQueue.put(iTruffleCommand);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
