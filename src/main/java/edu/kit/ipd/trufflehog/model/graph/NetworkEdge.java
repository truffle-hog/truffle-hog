package edu.kit.ipd.trufflehog.model.graph;

import java.io.Serializable;

public class NetworkEdge implements IConnection, Serializable {

	private long totalPacketCount;

	private int connectionType;

	private long active;

}
