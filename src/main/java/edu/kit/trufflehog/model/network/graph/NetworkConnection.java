package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.graph.components.edge.BasicEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastEdgeRendererComponent;
import edu.kit.trufflehog.model.network.graph.components.edge.MulticastLayeredEdgeRendererComponent;
import edu.kit.trufflehog.util.ICopyCreator;

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
    public NetworkConnection(INode networkNodeSrc, INode networkNodeDest, IComponent... components) {

        src = networkNodeSrc;
        dest = networkNodeDest;

        int result = 17;
        result = result + 31 * this.src.hashCode();
        result = result + 31 * this.dest.hashCode();
        hashcode = result;

        for (IComponent c : components) {
            this.addComponent(c);
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


    public IConnection createDeepCopy() {

        //TODO externalise

/*        final IConnection copy = new NetworkConnection(getSrc().createDeepCopy(), getDest().createDeepCopy());

        this.stream().forEach(component -> {
            if (component.isMutable()) {

                copy.addComponent(component.createDeepCopy());

            } else {
                copy.addComponent(component);
            }
        });

        return copy;*/
        return null;
    }


    @Override
    public IConnection createDeepCopy(ICopyCreator copyCreator) {
        return copyCreator.createDeepCopy(this);
    }

    /**
     * Updates this connection with the given connection
     * @param update the connection that updates this connection
     * @return true if this connection was updated, false if there was no success in updating
     *              or no values changes
     */
    @Override
    public boolean update(IComponent instance, IUpdater updater) {

        if (!this.equals(instance)) {
            // also implicit NULL check -> no check for null needed
            return false;
        }
        // if it is equal than it is an IConnection thus we can safely cast it
        final IConnection updateConnection = (IConnection) instance;

        return updater.update(this, updateConnection);
    }
}
