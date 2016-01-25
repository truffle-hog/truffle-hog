package edu.kit.ipd.trufflehog.util;

public abstract class Notifier<M> implements INotifier {

	public void addListener(IListener listener) {

	}

	public void removeListener(IListener listener) {

	}

	public void sendToListeners(M message) {

	}


	/**
	 * @see trufflehog.util.INotifier#attachListener(trufflehog.util.Listener)
	 * 
	 *  
	 */
	public void attachListener(Listener listener) {

	}


	/**
	 * @see trufflehog.util.INotifier#detach(trufflehog.util.Listener)
	 * 
	 *  
	 */
	public void detach(Listener listener) {

	}


	/**
	 * @see trufflehog.util.INotifier#notifyListeners(trufflehog.util.M)
	 * 
	 *  
	 */
	public void notifyListeners(M message) {

	}

}
