package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.uci.ics.jung.graph.Graph;

/**
 * TODO IMPLEMENT having replay network is necessary, because there are several internal functionalities that
 * TODO are handled a bite differently in contrast to a live network
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
