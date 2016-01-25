package edu.kit.ipd.trufflehog.presenter.commandQueue;

import java.util.BlockingQueue;

public class CommandQueueManager {

	private BlockingQueue availableElementQueue;

	private int currentQueue;

	private int registeredQueues;

	public void registerQueue(CommandQueue q) {

	}

	public CommandQueue getNextQueue() {
		return null;
	}

	public void notifyNewElement() {

	}

}
