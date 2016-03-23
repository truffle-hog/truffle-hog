package edu.kit.trufflehog.service.executor;

import edu.kit.trufflehog.command.ICommand;
import edu.kit.trufflehog.command.trufflecommand.ITruffleCommand;
import edu.kit.trufflehog.util.IListener;
import javafx.application.Platform;

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

            ICommand command = null;

            try {
                command = blockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Platform.runLater(command::execute);

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
