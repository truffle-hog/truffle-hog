package edu.kit.trufflehog.model.network;

/**
 * Created by jan on 19.02.16.
 */
public interface INetwork {

    INetworkReadingPort getReadingPort();

    INetworkWritingPort getWritingPort();

    INetworkViewPort getViewPort();
}
