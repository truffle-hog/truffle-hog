package edu.kit.trufflehog.model.network.recording;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetworkReadingPort;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;

/**
 * TODO implement
 */
public class NetworkReadingPortSwitch implements INetworkReadingPortSwitch {

    private INetworkReadingPort activePort;

    public NetworkReadingPortSwitch(INetworkReadingPort readingPort) {

        activePort = readingPort;
    }

    @Override
    public INetworkReadingPort getActiveReadingPort() {
        return activePort;
    }

    @Override
    public void setActiveReadingPort(INetworkReadingPort port) {
        activePort = port;
    }

    @Override
    public INode getNetworkNodeByAddress(IAddress address) {
        return getActiveReadingPort().getNetworkNodeByAddress(address);
    }

    @Override
    public IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest) {
        return getActiveReadingPort().getNetworkConnectionByAddress(source, dest);
    }
}
