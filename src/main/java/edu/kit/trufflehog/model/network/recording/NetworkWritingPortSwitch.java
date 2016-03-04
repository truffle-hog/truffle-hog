package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;

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
}
