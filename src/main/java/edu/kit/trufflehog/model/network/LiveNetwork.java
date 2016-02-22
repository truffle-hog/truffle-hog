package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.jungconcurrent.ConcurrentDirectedSparseGraph;

/**
 * Created by jan on 22.02.16.
 */
public class LiveNetwork implements INetwork {
    public LiveNetwork(ConcurrentDirectedSparseGraph<INode, IConnection> graph) {



    }

    @Override
    public INetworkReadingPort getReadingPort() {
        return null;
    }

    @Override
    public INetworkWritingPort getWritingPort() {
        return null;
    }

    @Override
    public INetworkViewPort getViewPort() {
        return null;
    }
}
