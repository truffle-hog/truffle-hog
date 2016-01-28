package edu.kit.trufflehog.service.replaylogging;

import edu.kit.trufflehog.model.graph.GraphProxy;
import edu.kit.trufflehog.model.graph.AbstractNetworkGraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * <p>
 *      The SnapshotLogger takes the current graph from the {@link GraphProxy} and takes a snapshot of it. This snapshot
 *      is then given to the {@link ReplayLogger} to help generate a {@link ReplayLog}.
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
    public AbstractNetworkGraph takeSnapshot() {
        throw new NotImplementedException();
    }
}
