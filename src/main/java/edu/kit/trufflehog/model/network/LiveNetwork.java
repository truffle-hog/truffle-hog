package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.jung.graph.ObservableUpdatableGraph;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.recording.NetworkCopy;
import edu.kit.trufflehog.util.ICopyCreator;

/**
 * Created by jan on 22.02.16.
 */
public class LiveNetwork implements INetwork {

    private final INetworkIOPort ioPort;
    private final INetworkViewPort viewPort;

    private final ObservableUpdatableGraph<INode, IConnection> observableGraph;

    public LiveNetwork(ObservableUpdatableGraph<INode, IConnection> graph) {

        this.observableGraph = graph;

        ioPort = new NetworkIOPort(graph);
        viewPort = new NetworkViewPort(graph);

        viewPort.getMaxThroughputProperty().bind(ioPort.getMaxThroughputProperty());
        viewPort.getMaxConnectionSizeProperty().bind(ioPort.getMaxConnectionSizeProperty());
    }

    public ObservableUpdatableGraph<INode, IConnection> getObservableGraph() {
        return observableGraph;
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
    public INetworkIOPort getRWPort() {
        return ioPort;
    }

    @Override
    public INetworkViewPort getViewPort() {
        return viewPort;
    }

    @Override
    public <T> T accept(NetworkVisitor<T> visitor) {
        return visitor.visit(this);
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
