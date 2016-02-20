package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.graph.IConnection;
import edu.kit.trufflehog.model.graph.INode;

/**
 * Created by jan on 19.02.16.
 */
public interface INetworkReadingPort {

    INode getNetworkNodeByAddress(IAddress address);

    IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest);

}
