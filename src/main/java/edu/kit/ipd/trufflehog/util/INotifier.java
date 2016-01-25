package edu.kit.ipd.trufflehog.util;

import edu.kit.ipd.trufflehog.presenter.commandQueue.CommandQueue;

public interface INotifier<M> extends CommandQueue {

	public void attachListener(Listener listener);

	public void detach(Listener listener);

	public void notifyListeners(M message);

}
