package edu.kit.trufflehog.model.network.graph;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;

/**
 * <p>
 *     Interface used to represent communication relations of nodes in the graph.
 * </p>
 */
public interface IConnection {

    long getTotalPacketCount();

    void setTotalPacketCount(long value);

    LongProperty getTotalPacketCountProperty();

    long getActive();

    void setActive(long value);

    LongProperty getActiveProperty();

    long getConnectionType();

    void setConnectionType(int value);

    IntegerProperty getConnectionTypeProperty();

}
