package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.recording.NetworkCopy;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;

/**
 * Created by jan on 22.02.16.
 */
public class LiveNetwork implements INetwork {

    private final INetworkIOPort ioPort;
    private final INetworkViewPort viewPort;

    public LiveNetwork(ObservableUpdatableGraph<INode, IConnection> graph) {

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

    @Override
    public NetworkCopy createDeepCopy(ICopyCreator copyCreator) {

        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

}
