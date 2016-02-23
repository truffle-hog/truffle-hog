package edu.kit.trufflehog.model.network.graph;

import java.io.Serializable;

/**
 * <p>
 *     Edge in the graph to represent a relation between two devices. Stores important statistics about the ongoing
 *     communication.
 * </p>
 */
public class NetworkConnection implements IConnection, Serializable {

    private final INode src;
    private final INode dest;

    private final int hashcode;

    public NetworkConnection(IConnection edge) {

        this.src = edge.getSrc();
        this.dest = edge.getDest();
        this.hashcode = edge.hashCode();
    }

    public NetworkConnection(INode networkNodeSrc, INode networkNodeDest) {
        src = networkNodeSrc;
        dest = networkNodeDest;

        int result = 17;
        result = result + 31 * this.src.hashCode();
        result = result + 31 * this.dest.hashCode();
        hashcode = result;

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
    public int compareTo(IConnection o) {
        return 0;
    }
}
