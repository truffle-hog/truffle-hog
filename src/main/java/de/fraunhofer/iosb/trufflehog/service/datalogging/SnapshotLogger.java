package de.fraunhofer.iosb.trufflehog.service.datalogging;

import de.fraunhofer.iosb.trufflehog.model.graph.GraphProxy;
import de.fraunhofer.iosb.trufflehog.model.graph.INetworkGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * <p>
 *      The SnapshotLogger takes the current graph from the {@link GraphProxy} and takes a snapshot of it. This snapshot
 *      is then given to the {@link DataLogger} to help generate a {@link DataLog}.
 * </p>
 */
public class SnapshotLogger {

    /**
     * <p>
     *     Creates a new SnapshotLogger object with a {@link GraphProxy} object. The GraphProxy object contains the
     *     graph (after the proxy design pattern) so that the SnapshotLogger can take a snapshot of the graph.
     * </p>
     *
     * @param graphProxy The proxy object that contains the graph so that the SnapshotLogger can take a snapshot of it.
     */
    public SnapshotLogger(GraphProxy graphProxy) {
    }

    /**
     * <p>
     *      Takes a snapshot of the current graph and returns the snapshot.
     * </p>
     *
     * @return A snapshot of the current graph.
     */
    public INetworkGraph takeSnapshot() {
        throw new NotImplementedException();
    }
}
