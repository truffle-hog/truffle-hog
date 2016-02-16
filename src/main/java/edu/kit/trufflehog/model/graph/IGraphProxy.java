package edu.kit.trufflehog.model.graph;

/**
 * The Proxy interface to be used for switching the active graph.
 */
public interface IGraphProxy {

    void setActive(INetworkGraph graph);
}
