package edu.kit.trufflehog.model.graph;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Valentin Kiechle on 12.02.2016.
 *
 * Graph dummy having what method we need for testing.
 */
public class AbstractNetworkGraphTestClassDummy extends AbstractNetworkGraph {

    List<NetworkNode> nodes = new LinkedList<NetworkNode>();
    Boolean testEdgeExisting = false;

    @Override
    public void addNetworkNode(NetworkNode node) {
        nodes.add(node);
    }

    @Override
    public NetworkEdge addNetworkEdge(NetworkNode from, NetworkNode to) {
        if (nodes.contains(from) && nodes.contains(to)) {
            testEdgeExisting = true;
        }
        return null;
    }

    @Override
    public NetworkNode getNetworkNodeByMACAddress(String macAddress) {
        for (NetworkNode node : nodes) {
            if (node.getMacAdress().equals(macAddress)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public NetworkNode getNetworkNodeByIPAddress(String ipAddress) {
        return null;
    }

    @Override
    public NetworkNode getNetworkNodeByDeviceName(String deviceName) {
        return null;
    }

    @Override
    public NetworkEdge getNetworkEdge(NetworkNode a, NetworkNode b) {
        if (testEdgeExisting) {
            return new NetworkEdge();
        }
        else return null;
    }

    public boolean hasConnection() {
        return testEdgeExisting;
    }
}