package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;

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

		if (!(o instanceof NetworkNode)) {
			return false;
		}
		final NetworkNode other = (NetworkNode) o;
		return getAddress().equals(other.getAddress());
	}

	@Override
    public String toString() {

        // FIXME just for debugging check component for null
        final NodeStatisticsComponent stc = this.getComponent(NodeStatisticsComponent.class);

        if (stc != null) {

            return this.getAddress() + " [" + this.getComponent(NodeStatisticsComponent.class).getCommunicationCount() + "]";
        } else {
            return this.getAddress().toString();
        }

    }


	@Override
	public String name() {
		return "Network Node";
	}

	@Override
	public <T> T accept(IComponentVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public void setParent(IComposition parent) {
		throw new UnsupportedOperationException("Operation not implemented yet");
	}

	@Override
	public IComposition getParent() {
		throw new UnsupportedOperationException("Operation not implemented yet");
	}
}
