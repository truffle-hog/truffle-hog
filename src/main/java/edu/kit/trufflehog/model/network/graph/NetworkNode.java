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

    /**
     * Updates the given component if it existis in this node
     * @param update the component to update this component
     * @return true if it exists and was updated, false otherwise
     */
    @Override
    public boolean update(IComponent update) {

        final IComponent existing = getComponent(update.getClass());

        if (existing == null) {
            return false;
        }

        return existing.update(update);
    }

    @Override
    public boolean update(INode update) {

        if (!this.equals(update)) {
            return false;
        }

        update.getComponents().stream().forEach(c -> {

            if (c.isMutable()) {
                final IComponent existing = getComponent(c.getClass());
                existing.update(c);
            }
        });

        return true;

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
        return address.toString();
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
