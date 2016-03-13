package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.graph.IConnection;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Map;

/**
 * Created by jan on 07.03.16.
 */
public class NetworkCopy {

    private final Collection<IConnection> connections;
    private final Map<IAddress, Point2D> locations;

    private final int maxThroughput;
    private final int maxConnectionSize;
    private final long viewTime;

    public NetworkCopy(Collection<IConnection> connections, Map<IAddress, Point2D> locations,
                       int maxThroughput, int maxConnectionSize,  long viewTime) {

        this.connections = connections;
        this.locations = locations;
        this.maxThroughput = maxThroughput;
        this.maxConnectionSize = maxConnectionSize;
        this.viewTime = viewTime;

    }

    public int getMaxThroughput() {
        return maxThroughput;
    }

    public int getMaxConnectionSize() {
        return maxConnectionSize;
    }

    public long getViewTime() {
        return viewTime;
    }

    public Point2D transform(IAddress address) {
        return locations.get(address);
    }

    public Collection<IConnection> getConnections() {
        return connections;
    }

    public String toString() {

        return "\nconnections: " + connections.size() + "\n"
                + "max throughput: " + maxThroughput + "\n"
                + "max connectionsize: " + maxConnectionSize + "\n"
                + connections.toString();

    }

}
