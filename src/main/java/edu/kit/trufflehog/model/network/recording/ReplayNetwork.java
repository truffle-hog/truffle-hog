package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.graph.IConnection;
import edu.kit.trufflehog.model.graph.INode;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.uci.ics.jung.graph.Graph;

/**
 * Created by jan on 20.02.16.
 */
public class ReplayNetwork implements INetwork {

    public ReplayNetwork(Graph<INode, IConnection> replayViewGraph) {
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
