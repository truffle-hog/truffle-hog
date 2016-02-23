package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;

import java.io.Serializable;

/**
 * <p>
 *     Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode implements Serializable, INode {

	private final IAddress address;
	private final int hashcode;

	public NetworkNode(IAddress address) {

		this.address = address;
		this.hashcode = address.hashCode();
	}

	public NetworkNode(INode node) {
		this.address = node.getAddress();
		this.hashcode = node.hashCode();
	}


	@Override
	public IAddress getAddress() {
		return address;
	}

	@Override
	public int compareTo(INode o) {
		return this.getAddress().compareTo(o.getAddress());
	}
}
