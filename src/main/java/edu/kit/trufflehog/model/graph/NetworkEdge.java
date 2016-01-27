package edu.kit.trufflehog.model.graph;

import javafx.beans.property.*;
import java.io.Serializable;

/**<p>
 * Edge in the graph to represent a relation between two devices. Stores important statistics about the ongoing communication.
 * </p>
 */
public class NetworkEdge implements IConnection, Serializable {

    private LongProperty totalPacketCount = new SimpleLongProperty();
    private LongProperty active = new SimpleLongProperty();
    private IntegerProperty connectionType = new SimpleIntegerProperty();

    /**<p>
     * Getter for the total packet count
     * </p>
     * @return total packet count
     */
    public final long getTotalPacketCount() {
        return totalPacketCount.get();
    }

    /**<p>
     * Setter for the total packet count
     * </p>
     * @param value New packet count value
     */
    public final void setTotalPacketCount(long value) {
        totalPacketCount.set(value);
    }

    /**<p>
     * Getter for the total packet count property
     * </p>
     * @return total packet property
     */
    public LongProperty getTotalPacketCountProperty() {
        return totalPacketCount;
    }

    /**<p>
     * Getter for the period of activity
     * </p>
     * @return period of activity
     */
    public final long getActive() {
        return active.get();
    }

    /**<p>
     * Setter for the period of activity
     * </p>
     * @param value set new period of activity
     */
    public final void setActive(long value) {
        active.set(value);
    }

    /**<p>
     * Getter for the period of activity property
     * </p>
     * @return period of activity property
     */
    public LongProperty getActiveProperty() {
        return active;
    }

    /**<p>
     * Getter for the connection type
     * </p>
     * @return connection type
     */
    public final long getConnectionType() {
        return connectionType.get();
    }

    /**<p>
     * Setter for the connection type
     * </p>
     * @param value New packet count value
     */
    public final void setConnectionType(int value) {
        connectionType.set(value);
    }

    /**<p>
     * Getter for connection type property
     * </p>
     * @return connection type property
     */
    public IntegerProperty getConnectionTypeProperty() {
        return connectionType;
    }
}
