package edu.kit.trufflehog.model.graph;


/**
 * <p>
 *     Provides access to the graph model.
 * </p>
 */
public class Network implements INetwork {
    @Override
    public void writeNode(INode node) {

    }

    @Override
    public void writeConnection(IConnection connection) {

    }

    @Override
    public INode getNodeByIPAddress(int ip) {
        return null;
    }

    @Override
    public INode getNodeByMACAddress(long mac) {
        return null;
    }

    @Override
    public INode getNodeByDeviceName(String name) {
        return null;
    }

    @Override
    public IConnection getConnection(INode src, INode dest) {
        return null;
    }
}
