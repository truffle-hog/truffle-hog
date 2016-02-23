package edu.kit.trufflehog.model.network.graph;

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

        final int sourceCompare = getSrc().compareTo(o.getSrc());

        if (sourceCompare == 0) {
            return getDest().compareTo(o.getDest());
        } else {
            return sourceCompare;
        }
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
}
