package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;

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
    public <T> T accept(IComponentVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }


    /**
     * Updates this connection with the given connection
     * @param newConnection the connection that updates this connection
     * @return true if this connection was updated, false if there was no success in updating
     *              or no values changes
     */
    @Override
    public boolean update(IComponent newConnection, IUpdater updater) {

        if (!this.equals(newConnection)) {
            // also implicit NULL check -> no check for null needed
            return false;
        }
        // if it is equal than it is an IConnection thus we can safely cast it
        final IConnection updateConnection = (IConnection) newConnection;

        if (updater.update(this, updateConnection)) {
            return true;
        };
        return false;
    }

    @Override
    public String toString() {

        // FIXME just for debugging check component for null
        final EdgeStatisticsComponent stc = this.getComponent(EdgeStatisticsComponent.class);

        if (stc != null) {

            return "( " + getSrc() + " ) --" + "[" + stc.getTraffic() +"]--> ( " + getDest() + " )";
        } else {
            return "( " + getSrc() + " ) ----> ( " + getDest() + " )";
        }



    }

}
