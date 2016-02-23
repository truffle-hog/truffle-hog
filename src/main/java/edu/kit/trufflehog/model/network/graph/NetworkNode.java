package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>
 *     Node in the graph to represent a device in the network. Stores important device data and logs.
 * </p>
 */
public class NetworkNode implements Serializable, INode {

	private final IAddress address;
	private final int hashcode;

	private final HashMap<Class<? extends IComponent>, IComponent> components = new HashMap<>();

	public NetworkNode(IAddress address) {

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
	public <T extends IComponent> void addComponent(T component) {

		if (components.containsKey(component.getClass())) {
			throw new RuntimeException("the Node already maintains this component type,"
					+ "you cannot add two components of the same class name: " + component);
		}
		components.put(component.getClass(), component);
	}

	@Override
	public void removeComponent(Class<? extends IComponent> type) {

		components.remove(type);
	}

	@Override
	public <T extends IComponent> T getComponent(Class<T> componentType) {

		if (componentType == null) {
			throw new NullPointerException("componentType must not be null");
		}

		final IComponent component = components.get(componentType);

		if (component != null) {

			// Safe to suppress unchecked as every value in the components
			// map that will be retrieved by the according class type will
			// be a component of that exact type
			@SuppressWarnings("unchecked")
			T existing = (T) component;

			return existing;
		} else {
			return null;
		}
	}

	@Override
	public int compareTo(INode o) {
		return this.getAddress().compareTo(o.getAddress());
	}


	@Override
	public String name() {
		return null;
	}

	@Override
	public boolean isMutable() {
		return true;
	}
}
