package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;

/**
 * This Interface provides the functionality needed to access network specific data.
 *
 * // TODO It can be augmented to have access functions with more functioanlity than te4h current
 * // TODO Just write an issue and create those functions and why we need them
 */
public interface INetworkReadingPort {

    /**
     * Returns a Network Node by providing an Address object. This can either be IPv4, IPv6, MAC...
     * @param address the address to be looking for
     * @return the Node that is identified with the given address
     */
    INode getNetworkNodeByAddress(IAddress address);

    /**
     * Return a Network Connection by providing a source and destination address of the connection
     * looking for.
     * @param source the source of the connection to be looking for
     * @param dest the destination of the connection to be looking fotr
     * @return the connection that is identified by the given source and destination addresses
     */
    IConnection getNetworkConnectionByAddress(IAddress source, IAddress dest);

}
