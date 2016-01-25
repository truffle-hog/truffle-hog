package edu.kit.ipd.trufflehog.model.graph;

import java.io.Serializable;

import edu.kit.ipd.trufflehog.model.graphlog.TruffleLogger;
import edu.kit.ipd.trufflehog.service.truffleprocessor.Truffle;

public class NetworkNode implements Serializable, INode {

	private String ipAdress;

	private String macAdress;

	private String deviceName;

	private int timeAdded;

	private int lastUpdateTime;

	private int packageCountIn;

	private int packageCountOut;

	private TruffleLogger truffleLogger;

	public TruffleLogger getLogger() {
		return null;
	}

	public void log(Truffle truffle) {

	}

}
