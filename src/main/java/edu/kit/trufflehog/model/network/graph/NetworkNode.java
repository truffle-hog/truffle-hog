package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;

import java.io.Serializable;

/**
 * <p>
 *     Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode implements Serializable, INode {

	private final IAddress address;
	private final int hashcode;
	private final IComposition composition;

	public NetworkNode(IAddress address) {
        super();

		this.address = address;
		this.hashcode = address.hashCode();

        composition = new SimpleComposition();

		// TODO maybe not make this default component
		composition.addComponent(new NodeStatisticsComponent(1));
	}

	@Override
	public IAddress getAddress() {
		return address;
	}

	@Override
	public INode createDeepCopy() {

		final INode node = new NetworkNode(address);

		composition.getComponents().stream().forEach(component -> {
			if (component.isMutable()) {

				composition.addComponent(component.createDeepCopy());

			} else {
				composition.addComponent(component);
			}
		});
		return node;
	}

    /*
    **
     * Updates the given component if it existis in this node
     * @param update the component to update this component
     * @return true if it exists and was updated, false otherwise
     *
	@Override
    public boolean update(IComponent update) {

        final IComponent existing = composition.getComponent(update.getClass());

        if (existing == null) {
            return false;
        }

        return existing.update(update);
    }
    */

    public boolean update(INode update) {

        if (!this.equals(update)) {
            return false;
        }

        update.getComposition().getComponents().stream().forEach(c -> {

            if (c.isMutable()) {
                final IComponent existing = composition.getComponent(c.getClass());
                existing.update(update);
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


	public String name() {
		return "Network Node";
	}

	public boolean isMutable() {
		return true;
	}

    public IComposition getComposition() {
        return composition;
    }
}
