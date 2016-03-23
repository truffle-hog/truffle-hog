package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.filter.IFilter;
import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.util.ICopyCreator;
import edu.uci.ics.jung.graph.ObservableUpdatableGraph;

import java.util.Collection;

// TODO implement
public class NetworkWritingPortSwitch implements INetworkWritingPortSwitch {

    private INetworkWritingPort activePort;

    public NetworkWritingPortSwitch(INetworkWritingPort writingPort) {

        this.activePort = writingPort;
    }

    @Override
    public INetworkWritingPort getActiveWritingPort() {
        return activePort;
    }

    @Override
    public void setActiveWritingPort(INetworkWritingPort writingPort) {
        activePort = writingPort;
    }

    @Override
    public void writeConnection(IConnection connection) {
        activePort.writeConnection(connection);
    }

    @Override
    public void writeNode(INode node) {
        activePort.writeNode(node);
    }

    @Override
    public void applyFilter(IFilter filter) {
        activePort.applyFilter(filter);
    }

    @Override
    public ObservableUpdatableGraph<INode, IConnection> getGraph() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Collection<IConnection> createDeepCopy(ICopyCreator copyCreator) {

        return activePort.createDeepCopy(copyCreator);
    }

    @Override
    public boolean isMutable() {
        return true;
    }
}
