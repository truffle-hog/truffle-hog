package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastLayeredEdgeRendererComponent;

/**
 * <p>
 *     Edge in the graph to represent a relation between two devices. Stores important statistics about the ongoing
 *     communication.
 * </p>
 */
public class NetworkConnection extends AbstractComposition implements IConnection {

    private final INode src;
    private final INode dest;

    private final int hashcode;
    public NetworkConnection(INode networkNodeSrc, INode networkNodeDest) {

        src = networkNodeSrc;
        dest = networkNodeDest;

        int result = 17;
        result = result + 31 * this.src.hashCode();
        result = result + 31 * this.dest.hashCode();
        hashcode = result;

        this.addComponent(new EdgeStatisticsComponent(1));

        if (networkNodeDest.getAddress().isMulticast()) {
            this.addComponent(new MulticastEdgeRendererComponent());
        } else {
            this.addComponent(new BasicEdgeRendererComponent());
        }
    }

    @Override
    public INode getSrc() {
        return src;
    }

    @Override
    public INode getDest() {
        return dest;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof IConnection)) {
            return false;
        }

        final IConnection other = (IConnection) o;

        return getDest().equals(other.getDest()) && getSrc().equals(other.getSrc());

    }

    @Override
    public int hashCode() {
        return hashcode;
    }


    @Override
    public String name() {
        return "Network Connection";
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public IConnection createDeepCopy() {

        final IConnection copy = new NetworkConnection(getSrc().createDeepCopy(), getDest().createDeepCopy());

        components.values().stream().forEach(component -> {
            if (component.isMutable()) {

                copy.addComponent(component.createDeepCopy());

            } else {
                copy.addComponent(component);
            }
        });

        return copy;
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
    public boolean update(IConnection update) {

        if (!this.equals(update)) {
            return false;
        }

        update.stream().filter(IComponent::isMutable).forEach(c -> {
            final IComponent existing = getComponent(c.getClass());
            existing.update(c);
        });
        return true;
    }
}
