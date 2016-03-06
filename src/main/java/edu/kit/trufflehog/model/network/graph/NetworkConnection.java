package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRendererComponent;

/**
 * <p>
 *     Edge in the graph to represent a relation between two devices. Stores important statistics about the ongoing
 *     communication.
 * </p>
 */
public class NetworkConnection implements IConnection {

    private final INode src;
    private final INode dest;
    private final IComposition composition;

    private final int hashcode;
    public NetworkConnection(INode networkNodeSrc, INode networkNodeDest) {

        src = networkNodeSrc;
        dest = networkNodeDest;
        composition = new SimpleComposition();

        int result = 17;
        result = result + 31 * this.src.hashCode();
        result = result + 31 * this.dest.hashCode();
        hashcode = result;

        composition.addComponent(new EdgeStatisticsComponent(1));

        if (networkNodeDest.getAddress().isMulticast()) {
            composition.addComponent(new MulticastEdgeRendererComponent());
        } else {
            composition.addComponent(new BasicEdgeRendererComponent());
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


    public String name() {
        return "Network Connection";
    }

    public boolean isMutable() {
        return true;
    }

    @Override
    public IConnection createDeepCopy() {

        final IConnection copy = new NetworkConnection(getSrc().createDeepCopy(), getDest().createDeepCopy());

        composition.getComponents().stream().forEach(component -> {
            if (component.isMutable()) {

                copy.getComposition().addComponent(component.createDeepCopy());

            } else {
                copy.getComposition().addComponent(component);
            }
        });

        return copy;
    }

    /*
     * Updates the given component if it existis in this node
     * @param update the component to update this component
     * @return true if it exists and was updated, false otherwise
     *
    @Override
    public boolean update(INode update) {

        final IComponent existing = composition.getComponent(update.getClass());

        if (existing == null) {
            return false;
        }

        return existing.update(update);
    }
    */

    @Override
    public boolean update(IConnection update) {

        if (!this.equals(update)) {
            return false;
        }
        /*TODO fix this
        update.getComposition().getComponents().stream().forEach(c -> {

            if (c.isMutable()) {
                final IComponent existing = getComponent(c.getClass());
                existing.update(c);
            }
        });
        */
        return true;

    }

    @Override
    public IComposition getComposition() {
        return composition;
    }
}
