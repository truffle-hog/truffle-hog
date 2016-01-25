package edu.kit.ipd.trufflehog.service.truffleprocessor;

import edu.kit.ipd.trufflehog.util.Notifier;
import edu.kit.ipd.trufflehog.presenter.commandQueue.CommandQueue;

public abstract class TruffleReceiver extends Thread, Notifier implements Runnable {

	private TruffleManager truffleManager;

	private CommandQueue commandQueue;

	public abstract void run();

	void TruffleReceiver() {

	}

}
