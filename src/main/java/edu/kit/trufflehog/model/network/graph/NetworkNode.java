package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.util.ICopyCreator;

import java.io.Serializable;

/**
 * <p>
 *     Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode extends AbstractComposition implements Serializable, INode {


	private final IAddress address;
	private final int hashcode;

    public NetworkNode(IAddress address, IComponent... components) {
        super();

        this.address = address;
        this.hashcode = this.address.hashCode();

        for (IComponent c : components) {
            this.addComponent(c);
        }
    }

	@Override
	public IAddress getAddress() {
		return address;
	}

	@Override
	public INode createDeepCopy(ICopyCreator copyCreator) {

		copyCreator.createDeepCopy(this);

		final INode node = new NetworkNode(address);

		components.values().stream().forEach(component -> {
			if (component.isMutable()) {


				node.addComponent(component.createDeepCopy(copyCreator));

			} else {
				node.addComponent(component);
			}
		});
		return node;
	}

	/**
	 * Updates this node with the given node
	 * @param update the node that updates this node
	 * @return true if the update was successful and values change, false otherwise
	 */
	@Override

	public boolean update(IComponent update, IUpdater updater) {

		if (!this.equals(update)) {
			// also implicit NULL check -> no check for null needed
			return false;
		}
		// if it is equal than it is an INode thus we can safely cast it
		final INode updateNode = (INode) update;

		return updater.update(this, updateNode);


	}

	@Override
	public int hashCode() {

		return hashcode;
	}

	@Override
	public boolean equals(Object o) {

		if (!(o instanceof INode)) {
			return false;
		}
		final INode other = (INode) o;
		return getAddress().equals(other.getAddress());
	}

	@Override
    public String toString() {

        // FIXME just for debugging check component for null
        final NodeStatisticsComponent stc = this.getComponent(NodeStatisticsComponent.class);

        if (stc != null) {

            return this.getAddress() + " [" + this.getComponent(NodeStatisticsComponent.class).getThroughput() + "]";
        } else {
            return this.getAddress().toString();
        }

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
