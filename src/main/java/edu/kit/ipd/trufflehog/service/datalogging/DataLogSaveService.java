package edu.kit.ipd.trufflehog.service.datalogging;

import edu.kit.ipd.trufflehog.util.Listener;

import java.util.BlockingQueue;

public class DataLogSaveService implements Listener, Runnable {

	private BlockingQueue<ICommand> dateLogs;

	public void run() {

	}


	/**
	 * @see trufflehog.util.Listener#receive(trufflehog.util.M)
	 * 
	 *  
	 */
	public void receive(M m) {

	}

}
