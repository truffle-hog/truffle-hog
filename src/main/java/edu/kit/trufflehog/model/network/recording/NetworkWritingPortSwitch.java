package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.INetworkWritingPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;

// TODO implement
public class NetworkWritingPortSwitch implements INetworkWritingPortSwitch {
    public NetworkWritingPortSwitch(INetworkWritingPort writingPort) {
    }

    @Override
    public INetworkWritingPort getActiveWritingPort() {
        return null;
    }

    @Override
    public void setActiveWritingPort(INetworkWritingPort writingPort) {

    }

    @Override
    public void writeConnection(IConnection connection) {

    }

    @Override
    public void writeNode(INode node) {

    }
}
