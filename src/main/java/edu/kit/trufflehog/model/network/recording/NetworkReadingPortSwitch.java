package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;

/**
 * TODO implement
 */
public class NetworkReadingPortSwitch implements INetworkReadingPortSwitch {
    public NetworkReadingPortSwitch(INetworkReadingPort readingPort) {
    }

    @Override
    public INetworkReadingPort getActiveReadingPort() {
        return null;
    }

    @Override
    public void setActiveReadingPort(INetworkReadingPort port) {

    }

    @Override
    public INode getNetworkNodeByAddress(IAddress address) {
        return null;
    }

    @Override
    public IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest) {
        return null;
    }
}
