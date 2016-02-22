package edu.kit.trufflehog.model.network.graph;

import edu.kit.trufflehog.model.network.IAddress;
import edu.kit.trufflehog.model.network.INetworkReadingPort;

/**
 * Created by jan on 22.02.16.
 */
public class NetworkReadingPort implements INetworkReadingPort {


    @Override
    public INode getNetworkNodeByAddress(IAddress address) {
        return null;
    }

    @Override
    public IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest) {
        return null;
    }
}
