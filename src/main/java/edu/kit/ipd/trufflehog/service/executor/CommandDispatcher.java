package edu.kit.ipd.trufflehog.service.executor;

import edu.kit.ipd.trufflehog.presenter.commandQueue.CommandQueue;
import edu.kit.ipd.trufflehog.util.Listener;
import edu.kit.ipd.trufflehog.util.IListener;

public class CommandDispatcher extends Thread implements Listener, Runnable {

	private CommandQueue commandQueue;

	private CommandQueue commandQueue;

	public void run() {

	}

	public IListener asTruffleCommandListener() {
		return null;
	}

	public IListener asUserCommandListener() {
		return null;
	}


	/**
	 * @see trufflehog.util.Listener#receive(trufflehog.util.M)
	 * 
	 *  
	 */
	public void receive(M m) {

	}

}
