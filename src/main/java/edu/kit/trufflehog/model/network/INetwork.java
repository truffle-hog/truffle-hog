package edu.kit.trufflehog.model.network;

/**
 * This interface inhabits the functionality that the network representation in Trufflehog uses.
 */
public interface INetwork {

    /**
     * Returns the reading port of this network.
     * @return The reading port of this network
     */
    INetworkReadingPort getReadingPort();

    /**
     * Returns the writing port of this network.
     * @return the writign port of this network
     */
    INetworkWritingPort getWritingPort();

    /**
     * Returns the view port of this network. This will be accessed by view elements that need to access
     * data about the network. The view port also needs to provide access to all metadata of this network
     * (e.g. maximum number of connections, illegal connection count, etc...)
     * @return the view port of this network
     */
    INetworkViewPort getViewPort();
}
