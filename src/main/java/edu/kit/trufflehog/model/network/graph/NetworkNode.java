package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;

import java.io.Serializable;

/**
 * <p>
 *     Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode extends AbstractComposition implements Serializable, INode, IComposition {

	private final IAddress address;
	private final int hashcode;

	public NetworkNode(IAddress address) {
        super();

		this.address = address;
		this.hashcode = address.hashCode();
	}

	@Override
	public IAddress getAddress() {
		return address;
	}

	@Override
	public INode createDeepCopy() {

		final INode node = new NetworkNode(address);

		components.values().stream().forEach(component -> {
			if (component.isMutable()) {

				node.addComponent(component.createDeepCopy());

			} else {
				node.addComponent(component);
			}
		});
		return node;
	}



	@Override
	public int compareTo(INode o) {
		return this.getAddress().compareTo(o.getAddress());
	}


	@Override
	public String name() {
		return "Network Node";
	}

	@Override
	public boolean isMutable() {
		return true;
	}
}
