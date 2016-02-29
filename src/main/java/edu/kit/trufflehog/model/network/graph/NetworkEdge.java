package edu.kit.trufflehog.model.network.graph;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

import java.io.Serializable;

/**
 * <p>
 *     Edge in the graph to represent a relation between two devices. Stores important statistics about the ongoing
 *     communication.
 * </p>
 */
public class NetworkEdge implements IConnection, Serializable {

    private INode srcNode;
    private INode destNode;

    private LongProperty totalPacketCount = new SimpleLongProperty();
    private LongProperty active = new SimpleLongProperty();
    private IntegerProperty connectionType = new SimpleIntegerProperty();

    NetworkEdge(INode src, INode dest) throws NullPointerException{
        if (src == null) throw new NullPointerException("Source node is null!");
        if (dest == null) throw new NullPointerException("Destination node is null!");
        srcNode = src;
        destNode = dest;
    }

    @Override
    public INode getSource() {
        return srcNode;
    }

    @Override
    public INode getDestination() {
        return destNode;
    }

    /**
     * <p>
     *     Getter for the total packet count
     * </p>
     * @return total packet count
     */
    @Override
    public final long getTotalPacketCount() {
        return totalPacketCount.get();
    }

    /**
     * <p>
     *     Setter for the total packet count
     * </p>
     *
     * @param value New packet count value
     */
    @Override
    public final void setTotalPacketCount(long value) {
        totalPacketCount.set(value);
    }

    /**
     * <p>
     *     Getter for the total packet count property
     * </p>
     *
     * @return total packet property
     */
    @Override
    public LongProperty getTotalPacketCountProperty() {
        return totalPacketCount;
    }

    /**
     * <p>
     *     Getter for the period of activity
     * </p>
     *
     * @return period of activity
     */
    @Override
    public final long getActive() {
        return active.get();
    }

    /**
     * <p>
     *     Setter for the period of activity
     * </p>
     *
     * @param value set new period of activity
     */
    @Override
    public final void setActive(long value) {
        active.set(value);
    }

    /**
     * <p>
     *     Getter for the period of activity property
     * </p>
     *
     * @return period of activity property
     */
    @Override
    public LongProperty getActiveProperty() {
        return active;
    }

    /**
     * <p>
     *     Getter for the connection type
     * </p>
     *
     * @return connection type
     */
    @Override
    public final long getConnectionType() {
        return connectionType.get();
    }

    /**
     * <p>
     *     Setter for the connection type
     * </p>
     *
     * @param value New packet count value
     */
    @Override
    public final void setConnectionType(int value) {
        connectionType.set(value);
    }

    /**
     * <p>
     *     Getter for connection type property
     * </p>
     *
     * @return connection type property
     */
    @Override
    public IntegerProperty getConnectionTypeProperty() {
        return connectionType;
    }
}
