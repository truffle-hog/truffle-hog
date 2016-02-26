package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.NetworkIOPort;
import edu.kit.trufflehog.model.network.graph.NetworkViewPort;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;

/**
 * Created by jan on 22.02.16.
 */
public class LiveNetwork implements INetwork {

    private final INetworkIOPort ioPort;
    private final INetworkViewPort viewPort;

    public LiveNetwork(ConcurrentDirectedSparseGraph<INode, IConnection> graph) {

        ioPort = new NetworkIOPort(graph);
        viewPort = new NetworkViewPort(graph);

        viewPort.getMaxThroughputProperty().bind(ioPort.getMaxThroughputProperty());
        viewPort.getMaxConnectionSizeProperty().bind(ioPort.getMaxConnectionSizeProperty());
    }

    @Override
    public INetworkReadingPort getReadingPort() {
        return ioPort;
    }

    @Override
    public INetworkWritingPort getWritingPort() {
        return ioPort;
    }

    @Override
    public INetworkViewPort getViewPort() {
        return viewPort;
    }
}
